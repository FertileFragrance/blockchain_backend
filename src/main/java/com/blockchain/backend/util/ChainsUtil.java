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
     * @param address
     * @return 返回的是每条链上的余额的相加的结果
     */
    public  static  double getAllBlance(String address){
        double allBalance=0;
        List<BlockChain> blockChains=getBlockchains();
        for(int i=0;i<blockChains.size();i++){//遍历所有链
            allBalance+=blockChains.get(i).getBalance(address);
        }
        return  allBalance;
    }

}
