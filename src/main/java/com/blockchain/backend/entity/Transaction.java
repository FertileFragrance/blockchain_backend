package com.blockchain.backend.entity;

/**
 * 交易
 * @author 记得写上
 * TODO 完成此类后删除此行，注意使用javadoc注释
 */
public class Transaction {

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
    }

    /**
     * 生成哈希值
     * @return 哈希字符串
     */
    public String generateHash() {
        return "hash";
    }

}
