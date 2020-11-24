package com.blockchain.backend.util;

import com.blockchain.backend.pojo.chain.BlockChain;

import java.io.*;
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

    public static final String CHAIN_FILEPATH_ROOT = "src/main/resources/chains/";

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
    private static int difficulty = 5;

    /**
     * 目标字符串
     */
    private static String aimedStr = repeat(difficulty);

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

    /**
     * 查询地址上所有链上的总余额
     *
     * @param address 地址
     * @return 返回的是每条链上的余额的相加的结果
     */
    public static double getAllBalance(String address) {
        double allBalance = 0;
        // 遍历所有链
        for (BlockChain blockchain : BLOCKCHAINS) {
            allBalance += blockchain.getBalance(address);
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
        repeat(difficulty);
    }

    public static int getDifficulty() {
        return difficulty;
    }

    /**
     * 获取目标字符串
     */
    public static String getAimedStr() {
        return aimedStr;
    }

    /**
     * 重复字符串
     *
     * @param repeat 重复次数
     * @return 重复后字符串
     */
    private static String repeat(int repeat) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < repeat; i++) {
            buf.append("0");
        }
        return buf.toString();
    }

    public static void addNormalTransaction(String senderAddress, String recipientAddress, String senderPublicKey,
                                            String recipientPublicKey, String senderPrivateKey, double amount)
            throws RuntimeException {
        List<BlockChain> blockChains = getBlockchains();
        double needGive = amount;
        // 遍历每一条链
        for (BlockChain chain : blockChains) {
            // 首先判断转的金额是不是已经够了
            if (needGive == 0) {
                break;
            }
            // 对每一条链查询余额
            // 付款人在该链上没钱，跳过
            if (chain.getBalance(senderAddress) == 0) {
                continue;
            }
            // 如果有钱，且小于needGive,就全转过去,同时更新needGive
            if (chain.getBalance(senderAddress) <= needGive) {
                chain.addNormalTransaction(senderAddress, recipientAddress, senderPublicKey,
                        recipientPublicKey, senderPrivateKey, chain.getBalance(senderAddress));
                needGive = needGive - chain.getBalance(senderAddress);
            }
            // 如果钱多于needGive，则转needGive的数额过去,同时转账完成，跳出
            if (chain.getBalance(senderAddress) > needGive) {
                chain.addNormalTransaction(senderAddress, recipientAddress, senderPublicKey,
                        recipientPublicKey, senderPrivateKey, needGive);
                break;
            }
        }
    }

    /**
     * 序列化全部区块链
     */
    public static synchronized void serializeAllChains() {
        for (BlockChain blockchain : BLOCKCHAINS) {
            serializeBlockChain(blockchain);
        }
    }

    /**
     * 反序列化全部区块链
     */
    public static synchronized void deserializeAllChains(List<Long> nonces) {
        for (Long nonce : nonces) {
            deserializeBlockChain(nonce);
        }
    }

    /**
     * 序列化单个区块链对象
     *
     * @param chain 链
     */
    public static synchronized void serializeBlockChain(BlockChain chain) {
        try (FileOutputStream fos = new FileOutputStream(
                CHAIN_FILEPATH_ROOT + chain.getLastBlock().getBlockHead().getNonce() + ".txt");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(chain);
        } catch (IOException e) {
            System.err.println("block chain serialize error!");
            e.printStackTrace();
        }
    }

    /**
     * 反序列化单个区块链对象
     *
     * @param nonce 随机数
     */
    public static synchronized void deserializeBlockChain(long nonce) {
        try (FileInputStream fis = new FileInputStream(CHAIN_FILEPATH_ROOT + nonce + ".txt");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            BlockChain chain = (BlockChain) ois.readObject();
            boolean flag = true;
            for (BlockChain blockChain : BLOCKCHAINS) {
                if (nonce == blockChain.getLastBlock().getBlockHead().getNonce()) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                BLOCKCHAINS.add(chain);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("block chain deserialize error!");
        }
    }

}
