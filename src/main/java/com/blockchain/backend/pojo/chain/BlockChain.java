package com.blockchain.backend.pojo.chain;

import com.alibaba.fastjson.JSON;
import com.blockchain.backend.pojo.chain.block.Block;
import com.blockchain.backend.pojo.chain.block.tree.MerkleTree;
import com.blockchain.backend.pojo.chain.block.tree.transaction.Transaction;
import com.blockchain.backend.pojo.chain.block.tree.transaction.inandout.TransactionInput;
import com.blockchain.backend.pojo.chain.block.tree.transaction.inandout.TransactionOutput;
import com.blockchain.backend.util.ChainsUtil;

import java.util.*;

/**
 * 管理Block的链
 *
 * @author 听取WA声一片
 */
public class BlockChain {

    /**
     * 链中已有的最后一个区块
     */
    private Block lastBlock;

    /**
     * 构造方法，矿工找到随机数时调用
     *
     * @param nonce 随机数
     */
    public BlockChain(long nonce, String address) {
        this.lastBlock = new Block("I'm Genesis Block.", nonce, this, address);
        ChainsUtil.getBlockchains().add(this);
    }

    /**
     * 将一个新的区块加到链上，由管理区块链的工具类调用
     */
    public void addBlockToChain() {
        this.lastBlock = new Block(this);
    }

    /**
     * 获取链中最后一个区块
     *
     * @return 区块对象
     */
    public Block getLastBlock() {
        return lastBlock;
    }

    /**
     * 增加交易记录，可修改
     *
     * @param transactions 要增加的交易
     */
    public void addTransaction(Transaction[] transactions) {
        for (Transaction transaction : transactions) {
            this.lastBlock.addTransaction(transaction);
        }
    }

    /**
     * 余额查询
     *
     * @param address 发起查询的用户地址
     * @return 该条链上的未花费的交易输出
     */
    public double getBalance(String address) {
        Transaction[] transactions = this.getMyUTXOs(address);
        double total = 0;
        for (Transaction transaction : transactions) {
            total += transaction.getAmount();
        }
        return total;
    }

    /**
     * 找到目前未花费的output
     * TODO need to correct
     * @param address 发起查询的用户地址
     * @return 该条链上的未花费的交易
     */
    public Transaction[] getMyUTXOs(String address) {
        List<Transaction> unspent=new ArrayList<Transaction>();
        Block lastblock=this.lastBlock;//得到该条链的最新区块，最新区块中的merkletree记录了最完整的交易数据。
        MerkleTree merkleTree=lastblock.getMerkleTree();
        List<Transaction> transactions=merkleTree.getTransactions();
        List<String> transactionId=new ArrayList<>();
        //遍历交易,记录花费过的output
        for(int i=1;i<transactions.size();i++){
            Transaction transaction=transactions.get(i);
            TransactionInput transactionInput=transaction.getTransInput();
            if(transactionInput.getSenderAddress().equals(address)){//是该地址发出的input，故该input指向的output被使用过了
                transactionId.add(transactionInput.getTransactionId());
            }
        }
        //遍历交易输出，把未使用过的output加进去
        for(int i=1;i<transactions.size();i++) {
            boolean isUse=false;
            Transaction transaction = transactions.get(i);
            if(!transaction.getTransOutput().getRecipientAddress().equals(address)){//不是到address的output
                continue;
            }
            for (int j = 0; j < transactionId.size() - 1; j++) {
                if (transaction.getTransactionId().equals(transactionId.get(j))) {//该交易被使用过
                    isUse=true;
                    break;
                }
            }
            if (isUse==false) {//没用过
                unspent.add(transaction);
            }
        }

        Transaction[] unspentTransaction=new Transaction[unspent.size()];
        for(int i=0;i<unspentTransaction.length;i++){
            unspentTransaction[i]=unspent.get(i);
        }
        return unspentTransaction;

    }

    /**
     * 添加转账交易
     * @param sender
     * @param recipient
     * @param amount
     * @param blockChain
     */
    public void addNormalTransaction(String sender,String recipient,double amount,BlockChain blockChain){
        System.out.println("添加普通交易：   "+sender+"---->"+recipient+"   转账金额为： "+amount);
        Block lastblock=blockChain.getLastBlock();//得到最新区块
        MerkleTree merkleTree=lastblock.getMerkleTree();
        List<Transaction> transactionList=lastblock.getMerkleTree().getTransactions();//最新区块中的树包含了最全的交易信息，相当于最新的账本
        //钱不够，交易失败
        double resAmount=blockChain.getBalance(sender);
        if(resAmount<amount){
            System.out.println("余额不足，交易失败");
            return;
        }
        //遍历账本，找到足够的金额（ 即找到足够的到A中去的output，使这些output中的value和大于需要支付的amount)
        //同时，由于后续需要把output转input，故需要记录找到的output所在的交易的id，以及在output[]中的位置
        List<TransactionOutput> willpay=new ArrayList<TransactionOutput>();//用于存放找到的将要花费的output
        List<String> transactionId=new ArrayList<>();
        List<Double> amountOfEachTransaction=new ArrayList<>();
        double valueFound=0;
        A:for(int i=1;i<transactionList.size();i++){//遍历交易  从1开始遍历（不知道为什么会在创建区块链时merkletree里会自己加一个莫名奇妙的交易）
            Transaction thisTransaction=transactionList.get(i);
            TransactionOutput transactionOutputfalse=thisTransaction.getTransOutput();
            TransactionOutput transactionOutput=transactionOutputfalse;
            if(!transactionOutput.getRecipientAddress().equals(sender)){//不是sender的比特币，跳过
                continue ;
            }
            //是属于sender的比特币
            double amountOfThis=thisTransaction.getAmount();
            amountOfEachTransaction.add(amountOfThis);
            willpay.add(transactionOutput);
            valueFound+=thisTransaction.getAmount();
            transactionId.add(thisTransaction.getTransactionId());
            if(valueFound>amount){
                break A;
                }
            }
        //至此，willpay中记录了所有将要用于转账的output，TxidOutputIndex中记录了output所在的交易id，amountOfEachTransaction记录了每一笔交易的数额
        //output转input 即建立新的input，使input中的交易id能和 willpay中的output一 一对应起来
        TransactionInput []transactionInput=new TransactionInput[willpay.size()+1];//最后会多一笔找零交易，单独处理
        for(int i=0;i<transactionInput.length;i++){
            transactionInput[i]=new TransactionInput(null,sender);
        }
        for(int i=0;i<transactionInput.length-2;i++){//最后两笔input也单独处理
         transactionInput[i].setTransactionId(transactionId.get(i));
     }
        //至此，input构建完成，除了最后一笔找零
        //创建属于收款人的output,即到收款人去的output,最后2笔单独处理，因为可能会有找零
        TransactionOutput []toRecipient=new TransactionOutput[willpay.size()+1];//多一笔找零(到付款人地址去），找零可以是0

        Transaction transactionWillAdd[]=new Transaction[toRecipient.length];
        for(int i=0;i<toRecipient.length-2;i++){//最后一笔willpay需要拆分成两个交易
            toRecipient[i]=willpay.get(i);
            toRecipient[i].setRecipientAddress(recipient);
            Transaction transaction=new Transaction(transactionInput[i],toRecipient[i],amountOfEachTransaction.get(i));
            transactionWillAdd[i]=transaction;
        }
        //至此，willpay中仅剩最后一笔willpay
        Double lastAmount=amountOfEachTransaction.get(amountOfEachTransaction.size()-1);//最后一笔output的金额
        Double changeBack=valueFound-amount;//找零数目
        Double lastGive=lastAmount-changeBack;//最后一笔要给的金额
        transactionInput[transactionInput.length-2].setTransactionId(transactionId.get(transactionId.size()-1));
        toRecipient[toRecipient.length-2]=new TransactionOutput(recipient);
        Transaction transaction=new Transaction(transactionInput[transactionInput.length-2],toRecipient[toRecipient.length-2],lastGive);
        transactionWillAdd[transactionWillAdd.length-2]=transaction;
        transactionInput[transactionInput.length-1].setTransactionId(transactionId.get(transactionId.size()-1));
        toRecipient[toRecipient.length-1]=new TransactionOutput(sender);//找零
        Transaction lastTransaction=new Transaction(transactionInput[transactionInput.length-1],toRecipient[toRecipient.length-1],changeBack);
        transactionWillAdd[transactionWillAdd.length-1]=lastTransaction;

        //添加交易
        blockChain.addTransaction(transactionWillAdd);

    }
    public void addMinnerTransaction(String minnerAddress){
        Transaction transaction=new Transaction(minnerAddress);
        transaction=Transaction.newMinnerTransaction(transaction);
        Block lastBlock=this.getLastBlock();
        lastBlock.addTransaction(transaction);
    }

}
