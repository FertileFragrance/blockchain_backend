package com.blockchain.backend.vo;

import lombok.Data;

import java.util.List;

/**
 * @author njuselhx
 */
@Data
public class TransferAccountVO {

    private String senderName;
    private List<String> recipientNames;
    private List<Double> moneys;

}
