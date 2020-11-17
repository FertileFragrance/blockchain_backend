package com.blockchain.backend.service.serviceimpl;

import com.blockchain.backend.dao.UserMapper;
import com.blockchain.backend.pojo.chain.block.Block;
import com.blockchain.backend.pojo.user.User;
import com.blockchain.backend.pojo.chain.BlockChain;
import com.blockchain.backend.service.UserService;
import com.blockchain.backend.util.CalculateUtil;
import com.blockchain.backend.util.ChainsUtil;
import com.blockchain.backend.vo.ResponseVO;
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
        // TODO:将下列注释掉的方法实现
        return null;
    }
//    public static Block generateBlock(Block oldBlock, int vac) {
//        Block newBlock = new Block();
//        但是block类又没有无参构造，就算加个无参构造，这个区块的属性也得不到初始化，像MerkelTree一类的对象我都不知道怎么设置
//        newBlock.setPreHash(oldBlock.getHash());这里的oldBlockHash我不知道从哪获取
//        newBlock.setDifficulty(difficulty);
//
//        /*
//         * 这里的 for 循环很重要： 获得 i 的十六进制表示 ，将 Nonce 设置为这个值，并传入 calculateHash 计算哈希值。
//         * 之后通过Block类的isHashValid 函数判断是否满足难度要求，如果不满足就重复尝试。 这个计算过程会一直持续，直到求得了满足要求的
//         * Nonce 值，之后通过方法将新块加入到链上。
//         */
//        for (int i = 0;; i++) {
//            String hex = String.format("%x", i);
//            newBlock.setNonce(hex);
//            if (!isHashValid(calculateHash(newBlock), newBlock.getDifficulty())) {
//                System.out.printf("%s\n", calculateHash(newBlock));
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    LOGGER.error("error:", e);
//                }
//                continue;
//            } else {
//                System.out.printf("%s work done!\n", calculateHash(newBlock));
//                newBlock.setHash(calculateHash(newBlock));
//                break;
//            }
//        }
//        return newBlock;
//    }

    @Override
    public ResponseVO queryBalance(UserVO userVO) {
        // TODO
        List<com.blockchain.backend.entity.User> users = userMapper.findByUsername(userVO.getUsername());
        assert users.size() == 1;
        // BitcoinWallet wallet = users.get(0).getWallet();
        List<BlockChain> chains = ChainsUtil.getBlockchains();
        for (BlockChain chain : chains) {

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

}
