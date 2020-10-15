package com.blockchain.backend.util;

import com.blockchain.backend.entity.Block;

/**
 * 管理Block的链，仅当作工具类使用
 * @author 听取WA声一片
 */
public class ChainUtil {

    /**
     * 私有化构造方法，不允许创建实例
     */
    private ChainUtil() {
    }

    /**
     * 链的长度
     */
    private static int chainSize = 0;

    /**
     * 链中已有的最后一个区块
     */
    private static Block lastBlock = null;

    /**
     * 挖到第一个区块时初始化链
     * @param nonce 挖出的随机数
     */
    private static Block initChain(int nonce) {
        Block block = new Block("I'm Genesis Block.", nonce);
        chainSize++;
        lastBlock = block;
        return lastBlock;
    }

    /**
     * 将一个新挖出的区块加到链上
     * @param nonce 挖出的随机数
     */
    public static Block addToChain(int nonce) {
        if (chainSize == 0) {
            return initChain(nonce);
        }
        Block block = new Block(nonce);
        chainSize++;
        lastBlock = block;
        return lastBlock;
    }

    /**
     * 获取链中最后一个区块
     * @return 区块对象
     */
    public static Block getLastBlock() {
        return lastBlock;
    }

}
