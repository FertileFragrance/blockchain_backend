package com.blockchain.backend.entity;

/**
 * 链中的块
 * @author 听取WA声一片
 */
public class Block {

    /**
     * 块头
     */
    private BlockHead blockHead;

    /**
     * 当前区块的哈希指针
     */
    private String currentBlockHashPointer;

    /**
     * merkle树
     */
    private MerkleTree merkleTree;

    public Block(String previousBlockHashPointer, int nonce) {
        this.blockHead = new BlockHead(previousBlockHashPointer, nonce);
        this.merkleTree = new MerkleTree();
        /**
         * TODO 根据算法计算当前区块的哈希指针并存入this.currentBlockHashPointer
         */

    }

}
