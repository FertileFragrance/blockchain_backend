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
     double value;

    /**
     * 接收者地址（锁定脚本） ：接收方的公钥的哈希，可通过接收者地址反向推出，故在转账时只需知道地址。
     */
    String recipientAddress;
    /**
     * 公钥的hash值
     */
    private String pubkeyHASH;

    public void setValue(double value) {
        this.value = value;
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    public TransactionOutput(double value, String recipientAddress) {
        this.value = value;
        this.recipientAddress = recipientAddress;
    }

    @Override
    public TransactionOutput clone() {
        return new TransactionOutput(this.value, this.recipientAddress);
    }

}
