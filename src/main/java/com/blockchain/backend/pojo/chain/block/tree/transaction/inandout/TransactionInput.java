package com.blockchain.backend.pojo.chain.block.tree.transaction.inandout;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 交易输入类
 *
 * @author njuselhx
 */
@Getter
@Setter
@ToString
public class TransactionInput implements Serializable {

    private static final long serialVersionUID = 8438916272336407173L;

    /**
     * 所引用的UTXO所在的交易的ID
     */
    private String transactionId;

    /**
     * output的索引
     */
    private int outputIndex;

    /**
     * 交易发起者地址
     */
    private String senderAddress;

    /**
     * 解锁脚本，即发送者的私钥
     */
    private String unlockScript;

    public TransactionInput(String transactionId, String senderAddress, String privateKey) {
        this.transactionId = transactionId;
        this.senderAddress = senderAddress;
        this.unlockScript = privateKey;
    }

    @Override
    public TransactionInput clone() {
        return new TransactionInput(this.transactionId,  this.senderAddress, this.unlockScript);
    }

}
