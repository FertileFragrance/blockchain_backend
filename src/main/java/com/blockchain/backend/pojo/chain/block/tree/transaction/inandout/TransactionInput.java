package com.blockchain.backend.pojo.chain.block.tree.transaction.inandout;

import lombok.Getter;
import lombok.Setter;

/**
 * 交易输入类
 *
 * @author njuselhx
 */
@Getter
@Setter
public class TransactionInput {

    /**
     * 所引用的UTXO所在的交易的ID
     * TODO consider changing the current type to "TransactionOutput"
     */
    private String transactionId;

    /**
     * output的索引
     */
    private int outputIndex;

    /**
     * 交易发起者地址（解锁脚本，需要用到公钥和私钥）
     */

    private  String senderAddress;

    /**
     * 交易签名
     */
    private String signature;

    /**
     * 公钥
     */
    private String publicKey;

    public TransactionInput(String transactionId,  String senderAddress) {
        this.transactionId = transactionId;
        this.senderAddress = senderAddress;
    }

    @Override
    public TransactionInput clone() {
        return new TransactionInput(this.transactionId,  this.senderAddress);
    }

}
