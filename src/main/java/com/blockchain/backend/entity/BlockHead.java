package com.blockchain.backend.entity;

import lombok.Data;

/**
 * 块头
 * @author 听取WA声一片
 */
@Data
public class BlockHead {

    /**
     * 软件版本号
     */
    private final String softwareVersionNumber = "IntelliJ IDEA 2020";

    /**
     * 前一个块的哈希指针
     */
    private final String previousBlockHashPointer;

    /**
     * 时间戳，由获取系统时间得到
     */
    private final long timeStamp = System.currentTimeMillis() / 1000;

    /**
     * 随机数
     */
    private final long nonce;

    /**
     * 交易数
     */
    private int transactionNumber;

    public BlockHead(String previousBlockHashPointer, int nonce) {
        this.previousBlockHashPointer = previousBlockHashPointer;
        this.nonce = nonce;
        this.transactionNumber = 1;
    }

}
