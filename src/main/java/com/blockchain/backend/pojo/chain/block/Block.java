package com.blockchain.backend.pojo.chain.block;

import com.blockchain.backend.pojo.chain.block.head.BlockHead;
import com.blockchain.backend.pojo.chain.BlockChain;
import com.blockchain.backend.pojo.chain.block.tree.MerkleTree;
import com.blockchain.backend.pojo.chain.block.tree.transaction.Transaction;
import com.blockchain.backend.util.CalculateUtil;
import lombok.Getter;

import java.io.Serializable;
import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 链中的块
 *
 * @author 听取WA声一片
 */
@Getter
public class Block implements Serializable {

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
    public Block(String previousBlockHashPointer, long nonce, BlockChain belongingChain, String address) {
        this.blockHead = new BlockHead(previousBlockHashPointer, nonce);
        this.merkleTree = new MerkleTree(address);
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


    /**
     * 定时生成区块
     * @author OD
     * @param blockChain 希望增加区块的链
     */
//    public void timerBlock(BlockChain blockChain) {
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                Block lastBlock = blockChain.getLastBlock();
//                Block newBlock = new Block(lastBlock.belongingChain);
//            }
//        }, 120000, 120000);//调用方法120000ms后执行，每隔120000ms再次执行
//    }


    /**
     * 校验HASH的合法性
     * @param hash 哈希
     * @param difficulty 难度
     * @return 是否合法
     */
//    public boolean isHashValid(String hash, int difficulty) {
//        String prefix = repeat("0", difficulty);
//        return hash.startsWith(prefix);
//    }

    /**
     *重复字符串
     * @param str 字符串
     * @param repeat 重复次数
     * @return 重复后字符串
     */
    private static String repeat(String str, int repeat) {
        final StringBuilder buf = new StringBuilder();
        for (int i = 0; i < repeat; i++) {
            buf.append(str);
        }
        return buf.toString();
    }


}
