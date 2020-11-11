package com.blockchain.backend.pojo.chain.block.head;

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

    /**
     * 创世区块产生时调用此构造方法
     * @param previousBlockHashPointer 前一个区块的哈希指针，已经给定
     * @param nonce 挖到的随机数
     */
    public BlockHead(String previousBlockHashPointer, long nonce) {
        this.previousBlockHashPointer = previousBlockHashPointer;
        this.nonce = nonce;
        this.transactionNumber = 1;
    }

    /**
     * 区块复制时调用此构造方法
     * @param previousBlockHashPointer 区块链中最后一个区块的哈希指针
     * @param nonce 区块链中最后一个区块的随机数
     * @param transactionNumber 区块链中最后一个区块的交易数量
     */
    public BlockHead(String previousBlockHashPointer, long nonce, int transactionNumber) {
        this.previousBlockHashPointer = previousBlockHashPointer;
        this.nonce = nonce;
        this.transactionNumber = transactionNumber;
    }

    /**
     * 拷贝前一个区块头，但哈希指针不同
     * @return 新的区块头对象
     */
    public BlockHead clone(String previousBlockHashPointer) {
        return new BlockHead(previousBlockHashPointer, this.nonce, this.transactionNumber);
    }

}
