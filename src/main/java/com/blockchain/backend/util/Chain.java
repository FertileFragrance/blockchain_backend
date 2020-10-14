package com.blockchain.backend.util;

import com.blockchain.backend.entity.Block;

/**
 * 管理Block的链，仅当作工具类使用
 * @author 听取WA声一片
 */
public class Chain {

    /**
     * 私有化构造方法，不允许创建实例
     */
    private Chain() {
    }

    /**
     * 链的长度
     */
    public static int chainSize = 0;

    /**
     * 链中已有的最后一个区块
     */
    private static Block lastBlock = null;

    /**
     * 挖到第一个区块时初始化链
     */
    public static void initChain(int nonce) {
        Block block = new Block("I'm Genesis Block.", nonce);
        chainSize++;
        lastBlock = block;
    }

    /**
     * 将一个新挖出的区块加到链上
     * @param previousBlockHashPointer 前一个区块的哈希指针
     * @param nonce 挖出的随机数
     */
    public static void addToChain(String previousBlockHashPointer, int nonce) {
        Block block = new Block(previousBlockHashPointer, nonce);
        chainSize++;
        lastBlock = block;
    }

    /**
     * 获取链中最后一个区块
     * @return 区块对象
     */
    public static Block getLastBlock() {
        return lastBlock;
    }

}
