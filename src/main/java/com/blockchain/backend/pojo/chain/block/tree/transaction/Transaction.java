package com.blockchain.backend.pojo.chain.block.tree.transaction;

import com.alibaba.fastjson.JSON;
import com.blockchain.backend.pojo.chain.BlockChain;
import com.blockchain.backend.pojo.chain.block.Block;
import com.blockchain.backend.pojo.chain.block.tree.transaction.inandout.*;
import com.blockchain.backend.util.CalculateUtil;
import com.blockchain.backend.util.ChainsUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * @author OD
 */
@Setter
@Getter
public class Transaction {

    /**
     * 交易ID: 是每笔交易的标识，对交易做hash运算得到
     */
    private String transactionId;

    /**
     * 交易输入
     */
    private TransactionInput transInput;

    /**
     * 交易输出
     */
    private TransactionOutput transOutput;

    /**
     * 交易金额
     */
    private double amount;

    public Transaction(TransactionInput transInput, TransactionOutput transOutput,double amount) {
        this.transInput = transInput;
        this.transOutput = transOutput;
        this.amount=amount;
        this.setTractionId(this);
    }

    /**
     * 产生创世区块时调用此构造方法
     */
    public Transaction(String address) {
        this.transInput = null;
        this.transOutput = new TransactionOutput(address);
        this.amount = ChainsUtil.NUM_OF_BITCOINS;
        this.setTractionId(this);
    }

    /**
     * 设置交易ID
     *
     * @param transaction 需要设置id的交易
     */
    private void setTractionId(Transaction transaction) {
        String objectString = JSON.toJSONString(transaction);
        this.transactionId = CalculateUtil.applySha256(objectString);
    }

    /**
     * 挖矿交易：只有输出，没有有效的输入（无引用id，无索引，不需要签名）
     *
     * @param minerAddress 矿工地址
     * @return 返回挖矿交易
     */
    /*
     public static Transaction newMinerTransaction(String minerAddress) {
        System.out.println("创建新的挖矿交易");
        TransactionInput txInput = new TransactionInput(null, -1, "geniusblock");
        TransactionOutput txOutput = new TransactionOutput(10, minerAddress);
        Transaction transaction = new Transaction(new TransactionInput[]{txInput}, new TransactionOutput[]{txOutput});
        transaction.setTractionId(transaction);
        return transaction;
    }
     */

    /**
     * 挖矿交易
     * @param transaction
     * @return transaction 更改过后的transction
     */
    public  static  Transaction newMinnerTransaction(Transaction transaction){
        System.out.println("创建新的挖矿交易：");
        String minnerAdress=transaction.getTransOutput().getRecipientAddress();
        TransactionInput transactionInput=new TransactionInput(null,"geniusblock");
        TransactionOutput transactionOutput=new TransactionOutput(minnerAdress);
        //挖到矿了，对tranction进行更改，暂时设置挖矿获得金额为10
        transaction.setAmount(10);
        transaction.setTransInput(transactionInput);
        transaction.setTransOutput(transactionOutput);
        transaction.setTractionId(transaction);
        return transaction;
    }



    /**
     * 创建普通转账交易，内部逻辑如下：
     * 1.获取余额，若不足以转账，创建交易失败
     * 2.遍历账本，找到属于付款人的合适的金额
     * 3.将outputs转成inputs
     * 这里有一个重要逻辑是：当一个人A接收到一笔由B转来的比特币时，merkle树中记录了一笔交易B-A，交易类中input（从哪来）记录了B的地址，output（到哪去）记录了A
     * 的地址。所以当A使用这笔钱转账时，产生的新交易中，会把之前交易中的output转为input,每一笔output再转成input后，就意味着被使用过了。
     * 4创建输出，创建一个属于收款人的output
     * 5.如果有找零，创建属于付款人的output
     * 6.设置交易id
     * 7.返回交易结构
     *
     * @param sender    付款方
     * @param recipient 收款方
     * @param amount    金额
     * @param bc        区块链
     * @return transaction 普通交易
     */
    /*
    public static Transaction newNormalTransaction(String sender, String recipient, double amount, BlockChain bc) {
        System.out.println("创建普通交易：   " + sender + "---->" + recipient + "   转账金额为： " + amount);
        Block lastBlock = bc.getLastBlock();
        List<Transaction> transactionList = lastBlock.getMerkleTree().getTransactions();
        double resAmount = bc.getBalance(sender);
        if (resAmount < amount) {
            System.out.println("余额不足，交易失败");
            return null;
        }
        List<TransactionOutput> willPay = new ArrayList<>();
        Map<String, List<Integer>> txIdOutputIndex = new HashMap<>(100);
        double valueFound = 0;
        A:
        for (int i = 1; i < transactionList.size(); i++) {
            TransactionOutput[] transactionOutput = transactionList.get(i).getTransOutput();
            List<Integer> outputIndex = new ArrayList<>();
            for (int j = 0; j < transactionOutput.length; j++) {
                if (!transactionOutput[j].getRecipientAddress().equals(sender)) {
                    continue;
                }
                willPay.add(transactionOutput[j]);
                valueFound += transactionOutput[j].getValue();
                outputIndex.add(j);
                txIdOutputIndex.put(transactionList.get(i).transactionId, outputIndex);
                if (valueFound > amount) {
                    break A;
                }
            }
        }
        TransactionInput transactionInput = new TransactionInput[willPay.size()];
        Set keySet = txIdOutputIndex.keySet();
        Iterator<String> iterator = keySet.iterator();
        int j = 0;
        while (iterator.hasNext()) {
            String txId = iterator.next();
            List<Integer> index = txIdOutputIndex.get(txId);
            for (Integer integer : index) {
                transactionInput[j] = new TransactionInput(txId, integer, sender);
                j++;
            }
        }
        TransactionOutput toRecipient = new TransactionOutput[willPay.size() + 1];
        for (int i = 0; i < toRecipient.length - 2; i++) {
            toRecipient[i] = willPay.get(i);
            toRecipient[i].setRecipientAddress(recipient);
        }
        double changeBack = valueFound - amount;
        TransactionOutput lastOne = new TransactionOutput(willPay.get(willPay.size() - 1).getValue() - changeBack, recipient);
        toRecipient[toRecipient.length - 2] = lastOne;
        TransactionOutput back = new TransactionOutput(changeBack, sender);
        toRecipient[toRecipient.length - 1] = back;
        Transaction transaction = new Transaction(transactionInput, toRecipient);
        transaction.setTractionId(transaction);
        return transaction;
    }
     */

    /**
     * 复制时调用此构造方法
     *
     * @param transInput  上一个的交易输入
     * @param transOutput 上一个的交易输出
     * @param amount      上一个的交易金额
     */
    public Transaction(TransactionInput transInput, TransactionOutput transOutput, int amount) {
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
     * 生成哈希值
     * TODO not return a const after finished
     *
     * @return 哈希字符串
     */
    public String generateHash() {
        return "hash";
    }

    /**
     * 拷贝前一个交易信息
     *
     * @return 新的交易
     */
    @Override
    public Transaction clone() {
        TransactionInput transIn;
        if (this.transInput == null) {
            transIn = null;
        } else {
            transIn = this.transInput.clone();
        }
        TransactionOutput transOut;
        if (this.transOutput == null) {
            transOut = null;
        } else {
            transOut = this.transOutput.clone();
        }
        return new Transaction(transIn, transOut, this.amount);
    }


}
