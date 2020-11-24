package com.blockchain.backend.pojo.chain.block;

import com.blockchain.backend.pojo.chain.block.head.BlockHead;
import com.blockchain.backend.pojo.chain.BlockChain;
import com.blockchain.backend.pojo.chain.block.tree.MerkleTree;
import com.blockchain.backend.pojo.chain.block.tree.transaction.Transaction;
import com.blockchain.backend.util.CalculateUtil;
import lombok.Getter;

import java.io.Serializable;

/**
 * 链中的块
 *
 * @author 听取WA声一片
 */
@Getter
public class Block implements Serializable {

    private static final long serialVersionUID = 3658432024215900552L;

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
    public final String currentBlockHashPointer;

    /**
     * merkle树
     */
    protected final MerkleTree merkleTree;

    /**
     * 此区块所属的链
     */
    public final BlockChain belongingChain;

    /**
     * 挖矿难度
     */
    private int difficulty;

    /**
     * 当且仅当增加创始区块时会调用此构造方法
     *
     * @param previousBlockHashPointer 前一个区块的哈希指针，是个定值
     * @param nonce                    挖出的随机数
     */
    public Block(String previousBlockHashPointer, long nonce,
                 BlockChain belongingChain, String address, String publicKey) {
        this.blockHead = new BlockHead(previousBlockHashPointer, nonce);
        this.merkleTree = new MerkleTree(address, publicKey);
        this.currentBlockHashPointer = CalculateUtil.applySha256(previousBlockHashPointer
                + this.blockHead.getTimeStamp() + this.blockHead.getNonce());
        this.belongingChain = belongingChain;
    }

    /**
     * 增加普通区块时调用此构造方法
     */
    public Block(BlockChain belongingChain) {
        this.belongingChain = belongingChain;
        Block lastBlock = this.belongingChain.getLastBlock();
        this.blockHead = lastBlock.blockHead.clone(lastBlock.currentBlockHashPointer);
        this.merkleTree = lastBlock.merkleTree.clone();
        this.currentBlockHashPointer = CalculateUtil.applySha256(lastBlock.currentBlockHashPointer
                + this.blockHead.getTimeStamp() + this.blockHead.getNonce());
    }

    /**
     * 增加交易记录
     *
     * @param transaction 交易
     */
    public void addTransaction(Transaction transaction) {
        this.merkleTree.insertTransaction(transaction);
        this.blockHead.setTransactionNumber(this.blockHead.getTransactionNumber() + 1);
    }

}
