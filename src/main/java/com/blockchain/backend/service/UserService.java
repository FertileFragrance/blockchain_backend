package com.blockchain.backend.service;

import com.blockchain.backend.vo.ResponseVO;
import com.blockchain.backend.vo.UserVO;

/**
 * @author 听取WA声一片
 */
public interface UserService {

    /**
     * 用户注册
     * @param userVO 视图层用户对象
     * @return 包装好的视图层回应对象
     */
    ResponseVO register(UserVO userVO);

    /**
     * 用户登录
     * @param userVO 视图层用户对象
     * @return 包装好的视图层回应对象
     */
    ResponseVO login(UserVO userVO);

}
