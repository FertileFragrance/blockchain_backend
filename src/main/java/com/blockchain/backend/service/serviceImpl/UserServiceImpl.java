package com.blockchain.backend.service.serviceImpl;

import com.blockchain.backend.dao.UserMapper;
import com.blockchain.backend.entity.User;
import com.blockchain.backend.service.UserService;
import com.blockchain.backend.vo.ResponseVO;
import com.blockchain.backend.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.List;

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
        List<User> users = userMapper.findByUsername(userVO.getUsername());
        if (users.size() > 0) {
            assert users.size() == 1;
            return ResponseVO.buildFailure("user exist");
        }
        User user = new User(userVO.getUsername(), userVO.getPassword());
        userMapper.save(user);
        UserVO newUserVO = new UserVO();
        BeanUtils.copyProperties(user, newUserVO);
        return ResponseVO.buildSuccess("register success", newUserVO);
    }

    @Override
    public ResponseVO login(UserVO userVO) {
        List<User> users = userMapper.findByUsernameAndPassword(userVO.getUsername(), userVO.getPassword());
        if (users.size() == 0) {
            return ResponseVO.buildFailure("user not found");
        }
        assert users.size() == 1;
        UserVO newUserVO = new UserVO();
        BeanUtils.copyProperties(users.get(0), newUserVO);
        return ResponseVO.buildSuccess("login success", newUserVO);
    }

//    //获取sha256
//    public static String getSHA256str(String str) {
//        MessageDigest messageDigest;
//        String encodeStr = "";
//        try {
//            messageDigest = MessageDigest.getInstance("SHA-256");
//            byte[] hash = messageDigest.digest(str.getBytes("UTF-8"));
//            encodeStr = HexBin.encode(hash);
//        } catch (NoSuchAlgorithmException ne) {
//            ne.printStackTrace();
//        } catch (UnsupportedEncodingException ue) {
//            ue.printStackTrace();
//        }
//        return encodeStr;
//    }
//
//
//    //挖矿
//    public static boolean miningService(String blockHash, String[] blockData, int nonce) {
//        String nonceStr = Integer.toString(nonce);
//        StringBuilder newHash = new StringBuilder(nonceStr);
//        for (String str : blockData) {
//            newHash.append(str);
//        }
//        return blockHash.equals(getSHA256str(newHash.toString()));
//    }
//
//
//    //节点
//    public static void buildNode(){
//
//        }
//
//
//    //交易
//    public static String transactionService(String publicKey1, String publicKey){
//
//    }


}
