package com.blockchain.backend.service.serviceimpl;

import com.blockchain.backend.dao.UserMapper;
import com.blockchain.backend.pojo.chain.BlockChain;
import com.blockchain.backend.pojo.chain.block.Block;
import com.blockchain.backend.pojo.user.User;
import com.blockchain.backend.pojo.user.wallet.BitcoinWallet;
import com.blockchain.backend.service.UserService;
import com.blockchain.backend.util.CalculateUtil;
import com.blockchain.backend.util.ChainsUtil;
import com.blockchain.backend.vo.ResponseVO;
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

import static com.blockchain.backend.pojo.user.User.FILEPATH_ROOT;

/**
 * @author 听取WA声一片
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
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
        long nonce = 0;
        String hexHash;//十六进制的hash
        do {
            hexHash = CalculateUtil.applySha256(Long.toString(nonce));
            nonce ++;
        } while (hexHash.startsWith(ChainsUtil.getAimedStr()));
        BlockChain newBlockChain = new BlockChain(nonce, hexHash);
        //此处保存链
        return null;
    }

    @Override
    public ResponseVO queryBalance(UserVO userVO) {
        // TODO 在BlockChain类和ChainsUtil中添加查询单条链和所有链的余额后，完成此方法，用用户所有的地址遍历查询一遍
        List<com.blockchain.backend.entity.User> users = userMapper.findByUsername(userVO.getUsername());
        assert users.size() == 1;
        User userPojo = new User();
        BeanUtils.copyProperties(users.get(0), userPojo);
        userPojo.deserializeWallet();
        BitcoinWallet wallet = userPojo.getWallet();
        for (String address : wallet.getBitcoinAddresses()) {
            // 查询
        }
        return null;
    }

    @Override
    public ResponseVO addKeys(UserVO userVO) {
        List<com.blockchain.backend.entity.User> users = userMapper.findByUsername(userVO.getUsername());
        assert users.size() == 1;
        User userPojo = new User();
        BeanUtils.copyProperties(users.get(0), userPojo);
        userPojo.deserializeWallet();
        try (FileWriter fw = new FileWriter(FILEPATH_ROOT + userPojo.getUsername() + ".txt", false)) {
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
        // TODO 在BlockChain类和ChainsUtil中完成在单条链上和所有链的增加交易后，完成此方法
        // TODO 此方法统筹发送者不同的地址余额生成各个链上要增加的交易
        return null;
    }

}
