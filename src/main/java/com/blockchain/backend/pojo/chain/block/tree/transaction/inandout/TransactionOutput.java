package com.blockchain.backend.pojo.chain.block.tree.transaction.inandout;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author njuselhx
 */
@Getter
@Setter
@ToString
public class TransactionOutput implements Serializable {

    private static final long serialVersionUID = 3867003532551769516L;

    /**
     * 接收者地址
     */
    private String recipientAddress;

    /**
     * 锁定脚本，即接收者的公钥
     */
    private String lockScript;

    public TransactionOutput(String recipientAddress, String publicKey) {
        this.recipientAddress = recipientAddress;
        this.lockScript = publicKey;
    }

    @Override
    public TransactionOutput clone() {
        return new TransactionOutput(this.recipientAddress, this.lockScript);
    }

}
