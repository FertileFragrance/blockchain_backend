package com.blockchain.backend.entity.chain;

import com.blockchain.backend.entity.chain.block.Block;
import com.blockchain.backend.entity.chain.block.tree.transaction.Transaction;
import com.blockchain.backend.util.ChainsUtil;

/**
 * 管理Block的链
 * @author 听取WA声一片
 */
public class BlockChain {

    /**
     * 链中已有的最后一个区块
     */
    private Block lastBlock;

    /**
     * 构造方法，矿工找到随机数时调用
     * @param nonce 随机数
     */
    public BlockChain(int nonce) {
        this.lastBlock = new Block("I'm Genesis Block.", nonce, this);
        ChainsUtil.getBlockchains().add(this);
    }

    /**
     * 将一个新的区块加到链上，由管理区块链的工具类调用
     */
    public void addBlockToChain() {
        this.lastBlock = new Block(this);
    }

    /**
     * 获取链中最后一个区块
     * @return 区块对象
     */
    public Block getLastBlock() {
        return lastBlock;
    }

    /**
     * 增加交易记录
     * @param transaction 交易
     */
    public void addTransaction(Transaction transaction) {
        this.lastBlock.addTransaction(transaction);
    }

}
