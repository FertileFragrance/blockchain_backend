package com.blockchain.backend.service.serviceImpl;

import com.blockchain.backend.dao.UserMapper;
import com.blockchain.backend.pojo.user.User;
import com.blockchain.backend.pojo.chain.BlockChain;
import com.blockchain.backend.service.UserService;
import com.blockchain.backend.util.ChainsUtil;
import com.blockchain.backend.vo.ResponseVO;
import com.blockchain.backend.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
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
        if (users.size() > 0) {
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
        if (users.size() == 0) {
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
        // TODO
        return null;
    }

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
        try {
            FileWriter fw = new FileWriter(FILEPATH_ROOT + userPojo.getUsername() + ".txt", false);
            fw.flush();
            fw.close();
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

}
