package com.blockchain.backend.pojo.chain.block.tree.transaction.inandout;


import lombok.Getter;

/**
 * @author njuselhx
 */
@Getter
public class TransactionOutput {

    /**
     * 接收者地址（锁定脚本） ：接收方的公钥的哈希，可通过接收者地址反向推出，故在转账时只需知道地址。
     */
    private final String recipientAddress;

    /**
     * 公钥的hash值
     */
    private String publicKeyHash;

    public TransactionOutput(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    @Override
    public TransactionOutput clone() {
        return new TransactionOutput(this.recipientAddress);
    }

}
