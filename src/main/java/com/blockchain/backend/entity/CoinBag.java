package com.blockchain.backend.entity;

import java.util.List;

/**
 * 钱包：存放了所有的coins（链地址），同时拥有清算货币的方法
 * @author OD
 */
public class CoinBag {

    /**
     * 包含了该用户所有的拥有货币所有权的链的地址（关于地址的说法存疑）
     */
    protected String[] coinBagAddress;


    /**
     * 统筹后将用于交易的链表，[币数][链址][币数][链址][币数]......
     */
    protected List<String> transactionList;

    /**
     * 钱包统筹货币的方法
     */
    protected void countCoin(String traderAddress, String tradeValue) {
        int coinsTraded = Integer.parseInt(tradeValue);
        int coinsSingleChain;
        for (String chainAddress : coinBagAddress) {
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
