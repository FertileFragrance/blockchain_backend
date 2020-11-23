package com.blockchain.backend.util;

import com.blockchain.backend.pojo.chain.BlockChain;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理所有区块链的类，仅作工具类使用
 *
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
     * 挖矿难度
     */
    private static int difficulty = 6;

    /**
     * 目标字符串
     */
    private static final String AIMED_STR = repeat("0", difficulty);

    /**
     * 给每条区块链增加一个区块
     */
    public static void updateChains() {
        // TODO modify
        for (BlockChain blockchain : BLOCKCHAINS) {
            blockchain.addBlockToChain();
        }
    }

    public static List<BlockChain> getBlockchains() {
        return BLOCKCHAINS;
    }

    /**
     * 查询地址上所有链上的总余额
     *
     * @param address 地址
     * @return 返回的是每条链上的余额的相加的结果
     */
    public static double getAllBalance(String address) {
        double allBalance = 0;
        List<BlockChain> blockChains = getBlockchains();
        // 遍历所有链
        for (int i = 0; i < blockChains.size(); i++) {
            allBalance += blockChains.get(i).getBalance(address);
        }
        return allBalance;
    }

    /**
     * 设置和获取难度
     *
     * @param dif 难度
     */
    public static void setDifficulty(int dif) {
        difficulty = dif;
    }

    public static int getDifficulty() {
        return difficulty;
    }

    /**
     * 获取目标字符串
     */
    public static String getAimedStr() {
        return AIMED_STR;
    }

    /**
     * 重复字符串
     *
     * @param str    字符串
     * @param repeat 重复次数
     * @return 重复后字符串
     */
    private static String repeat(String str, int repeat) {
        final StringBuilder buf = new StringBuilder();
        for (int i = 0; i < repeat; i++) {
            buf.append(str);
        }
        return buf.toString();
    }

}
