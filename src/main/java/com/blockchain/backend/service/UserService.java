package com.blockchain.backend.service;

import com.blockchain.backend.vo.ResponseVO;
import com.blockchain.backend.vo.SetDefaultAddressVO;
import com.blockchain.backend.vo.TransferAccountVO;
import com.blockchain.backend.vo.UserVO;

/**
 * @author 听取WA声一片
 */
public interface UserService {

    /**
     * 用户注册
     *
     * @param userVO 视图层用户对象
     * @return 包装好的视图层回应对象
     */
    ResponseVO register(UserVO userVO);

    /**
     * 用户登录
     *
     * @param userVO 视图层用户对象
     * @return 包装好的视图层回应对象
     */
    ResponseVO login(UserVO userVO);

    /**
     * 用户挖矿
     *
     * @param userVO 视图层用户对象
     * @return 包装好的视图层回应对象
     */
    ResponseVO mine(UserVO userVO);

    /**
     * 用户查询余额
     *
     * @param userVO 视图层用户对象
     * @return 包装好的视图层回应对象
     */
    ResponseVO queryBalance(UserVO userVO);

    /**
     * 用户增加密钥对
     *
     * @param userVO 视图层用户对象
     * @return 包装好的视图层回应对象
     */
    ResponseVO addKeys(UserVO userVO);

    /**
     * 用户查询所有用户
     *
     * @return 包装好的视图层用户对象
     */
    ResponseVO queryAllUsers();

    /**
     * 用户转账
     *
     * @param transferAccountVO 视图层转账表单对象
     * @return 包装好的视图层回应对象
     */
    ResponseVO transferAccount(TransferAccountVO transferAccountVO);

    /**
     * 用户设置默认地址
     *
     * @param setDefaultAddressVO 视图层设置地址对象
     * @return 包装好的视图层回应对象
     */
    ResponseVO setDefaultAddress(SetDefaultAddressVO setDefaultAddressVO);

}
