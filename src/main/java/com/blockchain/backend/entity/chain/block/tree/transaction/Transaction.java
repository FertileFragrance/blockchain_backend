package com.blockchain.backend.entity.chain.block.tree.transaction;

import com.blockchain.backend.entity.chain.block.tree.transaction.inandout.*;
import com.blockchain.backend.util.ChainsUtil;

/**
 * 交易
 * @author OD
 * TODO 完成此类后删除此行，注意使用javadoc注释
 */
public class Transaction {

    /**
     * 交易输入
     */
    private final TransactionInput transInput;

    /**
     * 交易输出
     */
    private final TransactionOutput transOutput;

    /**
     * 交易金额
     */
    private final int amount;

    /**
     * 产生创世区块时调用此构造方法
     */
    public Transaction() {
        this.transInput = null;
        // TODO not null after modification
        this.transOutput = null;
        this.amount = ChainsUtil.NUM_OF_BITCOINS;
    }

    /**
     * 复制时调用此构造方法
     * @param transInput 上一个的交易输入
     * @param transOutput 上一个的交易输出
     * @param amount 上一个的交易金额
     */
    public Transaction(TransactionInput transInput, TransactionOutput transOutput, int amount) {
        this.transInput = transInput;
        this.transOutput = transOutput;
        this.amount = amount;
    }

    /**
     * 私钥签名方法
     */
    private boolean scripSig(String sender, String recipient) {
        return true;
    }


    /**
     * 统筹币值，进行各条链上币的转移
     */
//    private void startTransaction() {
//        CoinBag forTransaction = new CoinBag();
//        forTransaction.countCoin(recipientAddress, Integer.toString(amount));
//        int counter = 0;
//        String coinsSingleChain;
//        for (String coinsAddress : forTransaction.transactionList) {
//            if (counter % 2 == 0) {
//                coinsSingleChain = coinsAddress;
//            } else {
//                //Block.merkleTree.insertTransaction(sender, recipient, coinsSingleChain, coinsAddress);
//            }
//            counter++;
//        }
//        System.out.println("the transaction is finished");
//    }

    /**
     * 生成哈希值
     * TODO not return a const after finished
     * @return 哈希字符串
     */
    public String generateHash() {
        return "hash";
    }

    /**
     * 拷贝前一个交易信息
     * @return 新的交易
     */
    @Override
    public Transaction clone() {
        TransactionInput transIn;
        if (this.transInput == null) {
            transIn = null;
        } else {
            transIn = this.transInput.clone();
        }
        TransactionOutput transOut;
        // TODO cannot be null after modification, remove the judgement
        if (this.transOutput == null) {
            transOut = null;
        } else {
            transOut = this.transOutput.clone();
        }
        return new Transaction(transIn, transOut, this.amount);
    }

}
