package com.blockchain.backend.controller;

import com.blockchain.backend.service.UserService;
import com.blockchain.backend.vo.ResponseVO;
import com.blockchain.backend.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 听取WA声一片
 */
@RestController
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 处理用户注册请求
     * @param userVO 视图层用户对象
     * @return 包装好的视图层回应对象
     */
    @PostMapping("/register")
    public ResponseVO register(UserVO userVO) {
        return userService.register(userVO);
    }

    /**
     * 处理用户登录请求
     * @param userVO 视图层用户对象
     * @return 包装好的视图层回应对象
     */
    @PostMapping("/login")
    public ResponseVO login(UserVO userVO) {
        return userService.login(userVO);
    }

}
