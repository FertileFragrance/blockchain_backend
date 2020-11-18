package com.blockchain.backend.vo;

import lombok.Data;

/**
 * @author njuselhx
 */
@Data
public class TransferAccountVO {

    private String senderName;
    private String[] recipientNames;
    private Double[] moneys;

}
