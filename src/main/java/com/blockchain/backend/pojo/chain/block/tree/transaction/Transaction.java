package com.blockchain.backend.pojo.chain.block.tree.transaction;

import com.alibaba.fastjson.JSON;
import com.blockchain.backend.pojo.chain.block.tree.transaction.inandout.*;
import com.blockchain.backend.util.CalculateUtil;
import com.blockchain.backend.util.ChainsUtil;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
/**
 * 交易
 * @author OD
 * TODO 完成此类后删除此行，注意使用javadoc注释
 */
public class Transaction {
    /**
     * 交易ID: 是每笔交易的标识，对交易做hash运算得到
     */
    private String Transaction_ID;
    /**
     * 交易输入:一个或多个
     */
    private final TransactionInput[] transInput;

    /**
     * 交易输出：一个或多个
     */
    private final TransactionOutput[] transOutput;

    /**
     * 交易金额
     */
    private  int amount;

    public Transaction(TransactionInput[]inputs,TransactionOutput[]outputs){
        this.transInput = inputs;
        this.transOutput = outputs;
        this.SetTraction_ID();
    }

    /**
     * 产生创世区块时调用此构造方法
     */
    public Transaction() {
        this.transInput = null;
        // TODO not null after modification
        this.transOutput = null;
        this.amount = ChainsUtil.NUM_OF_BITCOINS;
        this.SetTraction_ID();
    }

    /**
     * 设置交易ID
     * @param
     */
    private void SetTraction_ID(){
        String objectstring= JSON.toJSONString(this);//将交易对象序列化，得到一个字符串
        String txID= CalculateUtil.applySha256(objectstring);//由得到的字符串hash出交易id
        this.Transaction_ID=txID;
    }

    /**
     * 挖矿交易：只有输出，没有有效的输入（无引用id，无索引，不需要签名）
     * @param minner_adress
     * @return
     */
    public static Transaction new_Minner_Transaction(String minner_adress){
        //挖矿交易的输入是特殊的输入： 无交易ID，没有交易输出索引（设为-1）
        TransactionInput txinput=new TransactionInput(null,-1,"geniusblock");
        TransactionOutput txoutput=new TransactionOutput(10,minner_adress);//每次挖矿交易向矿工地址输出一定数目的比特币，暂定为10
        Transaction transaction=new Transaction(new TransactionInput[]{txinput},new TransactionOutput[]{txoutput});
        transaction.SetTraction_ID();
        return  transaction;
    }
    /**
     * 复制时调用此构造方法
     * @param transInput 上一个的交易输入
     * @param transOutput 上一个的交易输出
     * @param amount 上一个的交易金额
     */
    public Transaction(TransactionInput[] transInput, TransactionOutput[] transOutput, int amount) {
        this.transInput = transInput;
        this.transOutput = transOutput;
        this.amount = amount;
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
//    private void startTransaction() {
//        CoinBag forTransaction = new CoinBag();
//        forTransaction.countCoin(recipientAddress, Integer.toString(amount));
//        int counter = 0;
//        String coinsSingleChain;
//        for (String coinsAddress : forTransaction.transactionList) {
//            if (counter % 2 == 0) {
//                coinsSingleChain = coinsAddress;
//            } else {
//                //Block.merkleTree.insertTransaction(sender, recipient, coinsSingleChain, coinsAddress);
//            }
//            counter++;
//        }
//        System.out.println("the transaction is finished");
//    }

    /**
     * 生成哈希值
     * TODO not return a const after finished
     * @return 哈希字符串
     */
    public String generateHash() {
        return "hash";
    }

    /**
     * 拷贝前一个交易信息
     * @return 新的交易
     */
    @Override
    public Transaction clone() {
        TransactionInput[] transIn;
        if (this.transInput == null) {
            transIn = null;
        } else {
            transIn = this.transInput.clone();
        }
        TransactionOutput[] transOut;
        // TODO cannot be null after modification, remove the judgement
        if (this.transOutput == null) {
            transOut = null;
        } else {
            transOut = this.transOutput.clone();
        }
        return new Transaction(transIn, transOut, this.amount);
    }


}
