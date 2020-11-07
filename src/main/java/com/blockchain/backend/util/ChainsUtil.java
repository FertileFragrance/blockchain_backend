package com.blockchain.backend.util;

import com.blockchain.backend.entity.chain.BlockChain;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理所有区块链的类，仅作工具类使用
 * @author njuselhx
 */
public class ChainsUtil {

    /**
     * 私有化构造方法，不允许创建实例
     */
    private ChainsUtil() {
    }

    /**
     * 一条区块链的比特币数
     */
    public static final int NUM_OF_BITCOINS = 20;

    /**
     * 所有区块链组成的集合
     */
    private static final List<BlockChain> BLOCKCHAINS = new ArrayList<>();

    /**
     * 给每条区块链增加一个区块
     */
    public static void updateChains() {
        for (BlockChain blockchain : BLOCKCHAINS) {
            blockchain.addBlockToChain();
        }
    }

    public static List<BlockChain> getBlockchains() {
        return BLOCKCHAINS;
    }

}
