package com.blockchain.backend.entity.chain.block.tree.transaction.inandout;

/**
 * 交易输入类
 * @author njuselhx
 */
public class TransactionInput {

    /**
     * 交易发起者地址
     */
    private final String senderAddress;

    public TransactionInput(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    @Override
    public TransactionInput clone() {
        return new TransactionInput(this.senderAddress);
    }

}
