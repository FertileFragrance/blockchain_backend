package com.blockchain.backend.entity;

/**
 * 块头
 * @author 听取WA声一片
 */
public class BlockHead {

    /**
     * 软件版本号
     */
    private final String softwareVersionNumber = "IntelliJ IDEA 2020";

    /**
     * 前一个块的哈希指针
     */
    private String previousBlockHashPointer;

    /**
     * 时间戳，由获取系统时间得到
     */
    private final Long timeStamp = System.currentTimeMillis() / 1000;

    /**
     * 随机数
     */
    private int nonce;

    /**
     * 交易数
     */
    private int transactionNumber;

    public BlockHead(String previousBlockHashPointer, int nonce) {
        this.previousBlockHashPointer = previousBlockHashPointer;
        this.nonce = nonce;
        this.transactionNumber = 0;
    }

}
