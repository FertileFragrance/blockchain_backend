package com.blockchain.backend.pojo.chain.block.tree.transaction.inandout;

import lombok.Getter;

/**
 * @author njuselhx
 */
@Getter
public class TransactionOutput {

    /**
     * 输出金额
     */
    private int value;
    /**
     * 接收者地址（锁定脚本） ：接收方的公钥的哈希，可通过接收者地址反向推出，故在转账时只需知道地址。
     */
    private final String recipientAddress;


    public TransactionOutput(int value,String recipientAddress) {
        this.value=value;
        this.recipientAddress = recipientAddress;
    }

    @Override
    public TransactionOutput clone() {
        return new TransactionOutput(this.value,this.recipientAddress);
    }

}
