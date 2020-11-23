package com.blockchain.backend.dao;

import com.blockchain.backend.entity.Chain;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author njuselhx
 */
public interface ChainMapper extends CrudRepository<Chain, Integer> {

    /**
     * 通过随机数找链
     * @param nonce 随机数
     * @return 找到的Chain对象
     */
    List<Chain> findByNonce(long nonce);

}
