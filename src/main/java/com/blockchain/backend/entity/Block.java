package com.blockchain.backend.entity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 链中的块
 * @author 听取WA声一片
 */
@Getter
@Setter
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

    public Block(String previousBlockHashPointer, int nonce,List<Transaction>transactions) {
        this.blockHead = new BlockHead(previousBlockHashPointer, nonce,transactions);
        this.merkleTree = new MerkleTree();
        /**
         * TODO 根据算法计算当前区块的哈希指针并存入this.currentBlockHashPointer
         */

        this.currentBlockHashPointer=calculateHash(previousBlockHashPointer,nonce,this.blockHead.getTimeStamp());

    }

    public String calculateHash(String previousBlockHashPointer, int nonce,long timestamp){
        String hash=Util.applysha256(previousBlockHashPointer+Long.toString(timestamp)+Integer.toString(nonce));
        return hash;
    }

}
