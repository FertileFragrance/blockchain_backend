package com.blockchain.backend.entity;

import lombok.Getter;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * 钱包，存放用户私钥
 * @author OD
 */
@Getter
public class BitcoinWallet {

    /**
     * 私钥
     */
    private final ArrayList<String> privateKeys = new ArrayList<>();

    /**
     * 公钥，由私钥生成
     */
    private final ArrayList<Pair<String, String>> publicKeys = new ArrayList<>();

    /**
     * 比特币地址，由公钥生成
     */
    private final ArrayList<String> bitcoinAddresses = new ArrayList<>();

    /**
     * 统筹后将用于交易的链表，[币数][链址][币数][链址][币数]......
     */
    private List<String> transactionList;

    /**
     * 钱包统筹货币的方法
     */
    protected void countCoin(String traderAddress, String tradeValue) {
        int coinsTraded = Integer.parseInt(tradeValue);
        int coinsSingleChain;
        for (String chainAddress : bitcoinAddresses) {
            coinsSingleChain = coinsCheck(chainAddress);

            if (coinsSingleChain > 0 && coinsTraded > 0) {

                if (coinsTraded >= coinsSingleChain) {
                    transactionList.add(String.valueOf(coinsSingleChain));
                    coinsTraded -= coinsSingleChain;
                } else {
                    transactionList.add(String.valueOf(coinsTraded));
                    coinsTraded = 0;
                }
                transactionList.add(chainAddress);
            }

            if (coinsTraded == 0) {
                break;
            }

        }

        if (coinsTraded > 0) {
            System.out.println("there is a wrong because the recipient's account is not enough");
        }

    }


    /**
     * 查询单条链该用户所拥有的货币值
     */
    private int coinsCheck(String chainAddress) {
        return 0;
    }
}
