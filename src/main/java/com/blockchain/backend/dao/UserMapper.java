package com.blockchain.backend.dao;

import com.blockchain.backend.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author 听取WA声一片
 */
public interface UserMapper extends CrudRepository<User, Integer> {

    /**
     * 通过用户名和密码查询用户
     * @param username 用户名
     * @param password 密码
     * @return 查询结果的User对象
     */
    List<User> findByUsernameAndPassword(String username, String password);

    /**
     * 通过用户名查找用户
     * @param username 用户名
     * @return 查询结果的User对象
     */
    List<User> findByUsername(String username);

}
