package com.blockchain.backend.entity;

/**
 * 交易
 * @author 记得写上
 * TODO 完成此类后删除此行，注意使用javadoc注释
 */
public class Transaction {

        //暂时定义发起者使用现金交换数字货币
        //交易的唯一标识
    private  String id;
    //交易发起者地址
    private String sender;
    //交易接收者地址
    private  String recipient;
    //交易金额
    private int amount;


    public Transaction(String id,String sender,String recipient,int amount){
        this.id=id;
        this.sender=sender;
        this.recipient=recipient;
        this.amount=amount;
    }

    public Transaction() {
        if (scripSig(sender, recipient)) startTransaction();
        else System.out.println("it's a illegal transaction");
    }

    /**
     * 私钥签名方法
     */
    private boolean scripSig(String sender, String recipient) {
        return true;
    }




    /**
     * 统筹币值，进行各条链上币的转移
     */

    private void startTransaction(){
        CoinBag forTransaction = new CoinBag();
        forTransaction.countCoin(recipient, Integer.toString(amount));
        int counter = 0;
        String coinsSingleChain;
        for (String coinsAddress: forTransaction.transactionList) {
            if (counter%2 == 0) {
                coinsSingleChain = coinsAddress;
            } else {
                //Block.merkleTree.insertTransaction(sender, recipient, coinsSingleChain, coinsAddress);
            }
            counter ++;
        }
        System.out.println("the transaction is finished");
    }

    /**
     * 生成哈希值
     * @return 哈希字符串
     */
    public String generateHash() {
        return "hash";
    }

}
