package com.blockchain.backend.pojo.user.wallet;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 钱包，存放用户私钥
 *
 * @author OD
 */
@Data
public class BitcoinWallet implements Serializable {

    /**
     * 私钥
     */
    private final ArrayList<String> privateKeys = new ArrayList<>();

    /**
     * 公钥，由私钥生成
     */
    private final ArrayList<String> publicKeys = new ArrayList<>();

    /**
     * 比特币地址，由公钥生成
     */
    private final ArrayList<String> bitcoinAddresses = new ArrayList<>();

    /**
     * 默认地址，用户刚注册时为0
     */
    private int defaultAddressIndex = 0;

}
