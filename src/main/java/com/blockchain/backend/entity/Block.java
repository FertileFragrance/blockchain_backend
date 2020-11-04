package com.blockchain.backend.entity;

import com.blockchain.backend.util.CalculateUtil;
import com.blockchain.backend.util.ChainUtil;
import lombok.Getter;

/**
 * 链中的块
 * @author 听取WA声一片
 */
@Getter
public class Block extends GodBlock {

    /**
     * 魔数
     */
    private final int magicNumber = 0xD9B4BEF9;

    /**
     * 块头
     */
    private final BlockHead blockHead;


    /**
     * 当前区块的哈希指针
     */
    private final String currentBlockHashPointer;

    /**
     * merkle树
     */
    protected final MerkleTree merkleTree;

    /**
     *
     */
    String chainName ;




    /**
     * 当且仅当增加创始区块时会调用此构造方法
     * @param previousBlockHashPointer 前一个区块的哈希指针，是个定值
     * @param nonce 挖出的随机数
     */
    public Block(String previousBlockHashPointer, int nonce) {
        this.blockHead = new BlockHead(previousBlockHashPointer, nonce);
        this.merkleTree = new MerkleTree();
        this.currentBlockHashPointer = CalculateUtil.applySha256(previousBlockHashPointer
                + this.blockHead.getTimeStamp() + this.blockHead.getNonce());
    }

    /**
     * 增加普通区块时调用此构造方法
     * @param nonce 挖出的随机数
     */
    public Block(int nonce) {
        String previousBlockHashPointer = ChainUtil.getLastBlock().currentBlockHashPointer;
        this.blockHead = new BlockHead(previousBlockHashPointer, nonce);
        this.merkleTree = new MerkleTree();
        this.currentBlockHashPointer = CalculateUtil.applySha256(previousBlockHashPointer
                + this.blockHead.getTimeStamp() + this.blockHead.getNonce());
    }

    /**
     * 通过当前区块hash获取当前区块的构造方法
     */
    public Block(String currentBlockHashPointer){
        //无用nonce
        int nonce = 0;
        this.blockHead = new BlockHead(presentBlockHash, nonce);
        this.merkleTree = new MerkleTree();
        this.currentBlockHashPointer = currentBlockHashPointer;
    }

    /**
     * insertTransaction操作Transaction类里实现
     */
//    /**
//     * 增加交易记录
//     * @param transaction 交易
//     */
//    public void addTransaction(Transaction transaction) {
//        this.merkleTree.insertTransaction(transaction);
//        this.blockHead.setTransactionNumber(this.blockHead.getTransactionNumber() + 1);
//    }



}
