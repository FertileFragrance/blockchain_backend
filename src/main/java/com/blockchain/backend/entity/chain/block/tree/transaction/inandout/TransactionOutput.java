package com.blockchain.backend.entity.chain.block.tree.transaction.inandout;

/**
 * @author njuselhx
 */
public class TransactionOutput {

    /**
     * 接收者地址
     */
    private final String recipientAddress;


    public TransactionOutput(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    @Override
    public TransactionOutput clone() {
        return new TransactionOutput(this.recipientAddress);
    }

}
