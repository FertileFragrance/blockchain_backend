package com.blockchain.backend.service.serviceImpl;

import com.blockchain.backend.dao.UserMapper;
import com.blockchain.backend.entity.User;
import com.blockchain.backend.service.UserService;
import com.blockchain.backend.vo.ResponseVO;
import com.blockchain.backend.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
