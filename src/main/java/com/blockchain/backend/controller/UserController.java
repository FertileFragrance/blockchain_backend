package com.blockchain.backend.controller;

import com.blockchain.backend.service.UserService;
import com.blockchain.backend.vo.ResponseVO;
import com.blockchain.backend.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseVO register(@RequestBody UserVO userVO) {
        return userService.register(userVO);
    }

    /**
     * 处理用户登录请求
     * @param userVO 视图层用户对象
     * @return 包装好的视图层回应对象
     */
    @PostMapping("/login")
    public ResponseVO login(@RequestBody UserVO userVO) {
        return userService.login(userVO);
    }

    /**
     * 处理用户挖矿请求
     * @param userVO 视图层用户对象
     * @return 包装好的视图层回应对象
     */
    @PostMapping("/mine")
    public ResponseVO mine(@RequestBody UserVO userVO) {
        return userService.mine(userVO);
    }

    /**
     * 处理用户查询余额请求
     * @param userVO 视图层用户对象
     * @return 包装好的视图层回应对象
     */
    @GetMapping("/queryBalance")
    public ResponseVO queryBalance(@RequestBody UserVO userVO) {
        return userService.queryBalance(userVO);
    }

    /**
     * 处理用户增加密钥请求
     * @param userVO 视图层用户对象
     * @return 包装好的视图层回应对象
     */
    @PostMapping("/addKeys")
    public ResponseVO addKeys(@RequestBody UserVO userVO) {
        return userService.addKeys(userVO);
    }

    /**
     * 处理用户查询所有用户请求
     * @return 包装好的视图层用户对象
     */
    @GetMapping("/queryAllUsers")
    public ResponseVO queryAllUsers() {
        return this.userService.queryAllUsers();
    }

}
