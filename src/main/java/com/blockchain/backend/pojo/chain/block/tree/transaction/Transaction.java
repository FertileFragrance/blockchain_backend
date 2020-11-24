package com.blockchain.backend.pojo.chain.block.tree.transaction;

import com.alibaba.fastjson.JSON;
import com.blockchain.backend.pojo.chain.block.tree.transaction.inandout.*;
import com.blockchain.backend.util.CalculateUtil;
import com.blockchain.backend.util.ChainsUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author OD
 */
@Setter
@Getter
@ToString
public class Transaction implements Serializable {

    private static final long serialVersionUID = -7214356407311232149L;

    /**
     * 交易ID，每笔交易的标识，对交易做hash运算得到
     */
    private String transactionId;

    /**
     * 交易输入
     */
    private TransactionInput transInput;

    /**
     * 交易输出
     */
    private TransactionOutput transOutput;

    /**
     * 交易金额
     */
    private double amount;

    public Transaction(TransactionInput transInput, TransactionOutput transOutput, double amount) {
        this.transInput = transInput;
        this.transOutput = transOutput;
        this.amount = amount;
        this.setTractionId(this);
    }

    /**
     * 产生创世区块时调用此构造方法
     */
    public Transaction(String address, String publicKey) {
        this.transInput = null;
        this.transOutput = new TransactionOutput(address, publicKey);
        this.amount = ChainsUtil.NUM_OF_BITCOINS;
        this.setTractionId(this);
    }

    /**
     * 设置交易ID
     *
     * @param transaction 需要设置id的交易
     */
    private void setTractionId(Transaction transaction) {
        String objectString = JSON.toJSONString(transaction);
        this.transactionId = CalculateUtil.applySha256(objectString);
    }

    /**
     * 复制时调用此构造方法
     *
     * @param transInput  上一个的交易输入
     * @param transOutput 上一个的交易输出
     * @param amount      上一个的交易金额
     */
    public Transaction(TransactionInput transInput, TransactionOutput transOutput, int amount) {
        this.transInput = transInput;
        this.transOutput = transOutput;
        this.amount = amount;
    }

    /**
     * 生成哈希值
     *
     * @return 哈希字符串
     */
    public String generateHash() {
        return this.toString();
    }

    /**
     * 拷贝前一个交易信息
     *
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
        if (this.transOutput == null) {
            transOut = null;
        } else {
            transOut = this.transOutput.clone();
        }
        return new Transaction(transIn, transOut, this.amount);
    }

}
