package com.blockchain.backend.entity;

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
    public String toString(){
        return "id: "+this.id+"   sender: "+this.sender+"    recipient: "+this.recipient +"    amount: "+Integer.toString(amount);
    }
}
