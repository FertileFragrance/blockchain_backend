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
     */
    private String transactionID;

    /**
     * output的索引
     */
    private int output_INDEX;

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
    private String pubkey;

    public TransactionInput(String transactionID, int output_INDEX, String senderAddress) {
        this.transactionID = transactionID;
        this.output_INDEX = output_INDEX;
        this.senderAddress = senderAddress;
    }

    @Override
    public TransactionInput clone() {
        return new TransactionInput(this.transactionID, this.output_INDEX, this.senderAddress);
    }

}
