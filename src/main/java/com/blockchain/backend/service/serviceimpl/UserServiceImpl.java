package com.blockchain.backend.service.serviceimpl;

import com.blockchain.backend.dao.ChainMapper;
import com.blockchain.backend.dao.UserMapper;
import com.blockchain.backend.entity.Chain;
import com.blockchain.backend.pojo.chain.BlockChain;
import com.blockchain.backend.pojo.user.User;
import com.blockchain.backend.pojo.user.wallet.BitcoinWallet;
import com.blockchain.backend.service.UserService;
import com.blockchain.backend.util.CalculateUtil;
import com.blockchain.backend.util.ChainsUtil;
import com.blockchain.backend.vo.ResponseVO;
import com.blockchain.backend.vo.SetDefaultAddressVO;
import com.blockchain.backend.vo.TransferAccountVO;
import com.blockchain.backend.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.blockchain.backend.pojo.user.User.USER_FILEPATH_ROOT;

/**
 * @author 听取WA声一片
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserMapper userMapper;

    @Autowired
    private final ChainMapper chainMapper;

    public UserServiceImpl(UserMapper userMapper, ChainMapper chainMapper) {
        this.userMapper = userMapper;
        this.chainMapper = chainMapper;
    }


    @Override
    public ResponseVO register(UserVO userVO) {
        List<com.blockchain.backend.entity.User> users = userMapper.findByUsername(userVO.getUsername());
        if (!users.isEmpty()) {
            assert users.size() == 1;
            return ResponseVO.buildFailure("user exist");
        }
        com.blockchain.backend.entity.User userEntity =
                new com.blockchain.backend.entity.User(userVO.getUsername(), userVO.getPassword());
        User userPojo = new User(userEntity.getUsername(), userEntity.getPassword());
        userPojo.generateNewKeysAndAddress();
        userPojo.serializeWallet();
        userMapper.save(userEntity);
        UserVO newUserVO = new UserVO();
        BeanUtils.copyProperties(userPojo, newUserVO);
        return ResponseVO.buildSuccess("register success", newUserVO);
    }

    @Override
    public ResponseVO login(UserVO userVO) {
        List<com.blockchain.backend.entity.User> users =
                userMapper.findByUsernameAndPassword(userVO.getUsername(), userVO.getPassword());
        if (users.isEmpty()) {
            return ResponseVO.buildFailure("user not found");
        }
        assert users.size() == 1;
        User userPojo = new User();
        BeanUtils.copyProperties(users.get(0), userPojo);
        userPojo.deserializeWallet();
        UserVO newUserVO = new UserVO();
        BeanUtils.copyProperties(userPojo, newUserVO);
        return ResponseVO.buildSuccess("login success", newUserVO);
    }

    @Override
    public ResponseVO mine(UserVO userVO) {
        // TODO 在ChainsUtil类中设定挖矿标准后完成此方法
        /*
        String hexHash;
        Random random = new Random();
        ArrayList<Long> existedNonce = new ArrayList<>();
        long nonce;
        String blockhead = "65da5cs650c8eca98se5d4a654cc8e4asc8dca60aa6c486699";

        while1:
        do {
            nonce = random.nextLong();
            hexHash = CalculateUtil.applySha256(CalculateUtil.applySha256(Long.toString(nonce)+blockhead));
            System.out.println(nonce);
            if (existedNonce.size()!=0) {
                for (long existedNon: existedNonce) {
                    if (existedNon == nonce) {
                        continue while1;
                    }
                }
            }
        } while (!hexHash.startsWith(ChainsUtil.getAimedStr()));
        existedNonce.add(nonce);
        BlockChain newBlockChain = new BlockChain(nonce, hexHash);
        return ResponseVO.buildSuccess("mine success", newBlockChain.getLastBlock().getBlockHead().getNonce());
         */
        List<com.blockchain.backend.entity.User> users = userMapper.findByUsername(userVO.getUsername());
        assert users.size() == 1;
        User userPojo = new User();
        BeanUtils.copyProperties(users.get(0), userPojo);
        userPojo.deserializeWallet();
        BitcoinWallet bitcoinWallet=userPojo.getWallet();
        int defaultIndex=bitcoinWallet.getDefaultAddressIndex();
        String minerAddress=bitcoinWallet.getBitcoinAddresses().get(defaultIndex);
        long nonce = 0;
        // 十六进制的hash
        String hexHash;
        List<Chain> chains;
        while (true) {
            hexHash = CalculateUtil.applySha256(CalculateUtil.applySha256(Long.toString(nonce)+"65da5cs650c8eca98se5d4a654cc8e4asc8dca60aa6c486699"));
            if (hexHash.startsWith(ChainsUtil.getAimedStr())) {
                chains = chainMapper.findByNonce(nonce);
                if (chains.isEmpty()) {
                    BlockChain newBlockChain = new BlockChain(nonce, userPojo.getWallet()
                            .getBitcoinAddresses().get(userPojo.getWallet().getDefaultAddressIndex()));
                    newBlockChain.addMinerTransaction(minerAddress);
                    Chain chainEntity = new Chain(nonce);
                    chainMapper.save(chainEntity);
                    ChainsUtil.serializeBlockChain(newBlockChain);
                    return ResponseVO.buildSuccess("mine success", nonce);
                } else {
                    nonce++;
                }
            } else {
                nonce++;
            }
        }
    }

    @Override
    public ResponseVO queryBalance(UserVO userVO) {
        List<com.blockchain.backend.entity.User> users = userMapper.findByUsername(userVO.getUsername());
        assert users.size() == 1;
        User userPojo = new User();
        BeanUtils.copyProperties(users.get(0), userPojo);
        userPojo.deserializeWallet();
        BitcoinWallet wallet = userPojo.getWallet();
        List<Chain> chains = (List<Chain>) chainMapper.findAll();
        if (chains.size() != ChainsUtil.getBlockchains().size()) {
            List<Long> nonces = new ArrayList<>();
            for (Chain chain : chains) {
                nonces.add(chain.getNonce());
            }
            ChainsUtil.deserializeAllChains(nonces);
        }
        double total = 0;
        for (String address : wallet.getBitcoinAddresses()) {
            total += ChainsUtil.getAllBalance(address);
        }
        return ResponseVO.buildSuccess("query balance success", total);
    }

    @Override
    public ResponseVO addKeys(UserVO userVO) {
        List<com.blockchain.backend.entity.User> users = userMapper.findByUsername(userVO.getUsername());
        assert users.size() == 1;
        User userPojo = new User();
        BeanUtils.copyProperties(users.get(0), userPojo);
        userPojo.deserializeWallet();
        try (FileWriter fw = new FileWriter(USER_FILEPATH_ROOT + userPojo.getUsername() + ".txt", false)) {
            fw.flush();
        } catch (IOException e) {
            System.err.println("rewrite file error!");
            return ResponseVO.buildFailure("addKeys failure");
        }
        userPojo.generateNewKeysAndAddress();
        userPojo.serializeWallet();
        UserVO newUserVO = new UserVO();
        BeanUtils.copyProperties(userPojo, newUserVO);
        return ResponseVO.buildSuccess("addKeys success", newUserVO);
    }

    @Override
    public ResponseVO queryAllUsers() {
        List<com.blockchain.backend.entity.User> users =
                (List<com.blockchain.backend.entity.User>) userMapper.findAll();
        List<String> usernames = new ArrayList<>();
        for (com.blockchain.backend.entity.User user : users) {
            usernames.add(user.getUsername());
        }
        Collections.sort(usernames);
        return ResponseVO.buildSuccess("query all users success", usernames);
    }

    @Override
    public ResponseVO transferAccount(TransferAccountVO transferAccountVO) {
        List<com.blockchain.backend.entity.User> senders =
                userMapper.findByUsername(transferAccountVO.getSenderName());
        assert senders.size() == 1;
        User sender = new User();
        BeanUtils.copyProperties(senders.get(0), sender);
        sender.deserializeWallet();
        ArrayList<String> senderBitcoinAddress = sender.getWallet().getBitcoinAddresses();
        // TODO 在BlockChain类和ChainsUtil中完成在单条链上和所有链的增加交易后，完成此方法
        // TODO 此方法统筹发送者不同的地址余额生成各个链上要增加的交易
        String[] recipientNames = transferAccountVO.getRecipientNames();
        Double[] moneyNeedGive = transferAccountVO.getMoneys();
        // 遍历收款人，每个收款人都要收到一笔转账
        for (int i = 0; i < recipientNames.length; i++) {
            String recipentName = recipientNames[i];
            double moneyNeed = moneyNeedGive[i];
            // 获得默认收款地址
            List<com.blockchain.backend.entity.User> recipients = userMapper.findByUsername(recipentName);
            String recipientAddress = "";
            User recipient = new User();
            BeanUtils.copyProperties(recipients.get(i), recipient);
            recipient.deserializeWallet();
            int defaultAddressIndex = recipient.getWallet().getDefaultAddressIndex();
            ArrayList<String> recipientBitcoinAddress = recipient.getWallet().getBitcoinAddresses();
            recipientAddress = recipientBitcoinAddress.get(defaultAddressIndex);
            // 遍历付款人的地址
            for (int j = 0; j < senderBitcoinAddress.size(); j++) {
                String senderAddress = senderBitcoinAddress.get(j);
                // 查询该地址有多少钱
                double moneyOfSender = ChainsUtil.getAllBalance(senderAddress);
                if (moneyOfSender == 0) {
                    continue;
                }
                // 该地址余额小moneyNeedGive,则把所有钱全转过去,同时更新moneyWillGive
                if (moneyOfSender <= moneyNeed) {
                    ChainsUtil.addNormalTransaction(senderAddress, recipientAddress, moneyOfSender);
                    moneyNeed -= moneyOfSender;
                }
                // 该地址余额大于moneyWillGive，则转等额的钱即可，同时到该收款人的转账完成
                if (moneyOfSender > moneyNeed) {
                    ChainsUtil.addNormalTransaction(senderAddress, recipientAddress, moneyNeed);
                    moneyNeed = 0;
                    break;
                }
            }
        }
        return null;
    }

    @Override
    public ResponseVO setDefaultAddress(SetDefaultAddressVO setDefaultAddressVO) {
        List<com.blockchain.backend.entity.User> users =
                userMapper.findByUsername(setDefaultAddressVO.getUsername());
        assert users.size() == 1;
        User userPojo = new User();
        BeanUtils.copyProperties(users.get(0), userPojo);
        userPojo.deserializeWallet();
        int index = setDefaultAddressVO.getIndexNumber();
        if (index < 0 || index >= userPojo.getWallet().getBitcoinAddresses().size()) {
            return ResponseVO.buildFailure("wrong index number");
        }
        userPojo.getWallet().setDefaultAddressIndex(index);
        userPojo.serializeWallet();
        return ResponseVO.buildSuccess("set default address success");
    }

}
