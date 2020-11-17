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

@Setter
@Getter
/**
 * 交易
 * @author OD
 */
public class Transaction {
    /**
     * 交易ID: 是每笔交易的标识，对交易做hash运算得到
     */
    private String transactionId;
    /**
     * 交易输入:一个或多个
     */
    private  TransactionInput[] transInput;

    /**
     * 交易输出：一个或多个
     */
    private  TransactionOutput[] transOutput;

    /**
     * 交易金额
     */
    private  int amount;

    public Transaction(TransactionInput[]inputs,TransactionOutput[]outputs){
        this.transInput = inputs;
        this.transOutput = outputs;
        this.setTractionId(this);
    }

    /**
     * 产生创世区块时调用此构造方法
     */
    public Transaction() {
        this.transInput = null;
        this.transOutput = null;
        this.amount = ChainsUtil.NUM_OF_BITCOINS;
        this.setTractionId(this);
    }

    /**
     * 设置交易ID
     * @param transaction 需要设置id的交易
     */
    private void setTractionId(Transaction transaction){
        String objectstring= JSON.toJSONString(transaction);//将交易对象序列化，得到一个字符串
        String txId= CalculateUtil.applySha256(objectstring);//由得到的字符串hash出交易id
        this.transactionId =txId;
    }

    /**
     * 挖矿交易：只有输出，没有有效的输入（无引用id，无索引，不需要签名）
     * @param minnerAdress  矿工地址
     * @return transation   返回挖矿交易
     */
    public static Transaction newMinnerTransaction(String minnerAdress){
        //挖矿交易的输入是特殊的输入： 无交易ID，没有交易输出索引（设为-1）
        System.out.println("创建新的挖矿交易");
        TransactionInput txinput=new TransactionInput(null,-1,"geniusblock");
        TransactionOutput txoutput=new TransactionOutput(10,minnerAdress);//每次挖矿交易向矿工地址输出一定数目的比特币，暂定为10
        Transaction transaction=new Transaction(new TransactionInput[]{txinput},new TransactionOutput[]{txoutput});
        transaction.setTractionId(transaction);
        return  transaction;
    }

    /**
     * 创建普通转账交易，内部逻辑如下：
     * 1.获取余额，若不足以转账，创建交易失败
     * 2.遍历账本，找到属于付款人的合适的金额
     * 3.将outputs转成inputs
     *  这里有一个重要逻辑是：当一个人A接收到一笔由B转来的比特币时，merkle树中记录了一笔交易B-A，交易类中input（从哪来）记录了B的地址，output（到哪去）记录了A
     *  的地址。所以当A使用这笔钱转账时，产生的新交易中，会把之前交易中的output转为input,每一笔output再转成input后，就意味着被使用过了。
     * 4创建输出，创建一个属于收款人的output
     * 5.如果有找零，创建属于付款人的output
     * 6.设置交易id
     * 7.返回交易结构
     * @param sender 付款方
     * @param recipient 收款方
     * @param amount 金额
     * @param bc 区块链
     * @return transaction 普通交易
     */
    public static Transaction newnormalTransaction(String sender, String recipient, double amount, BlockChain bc){
        System.out.println("创建普通交易：   "+sender+"---->"+recipient+"   转账金额为： "+amount);
        Block lastblock=bc.getLastBlock();//得到最新区块
         List<Transaction> transactionList=lastblock.getMerkleTree().getTransactions();//最新区块中的树包含了最全的交易信息，相当于最新的账本
        //钱不够，交易失败
        double resAmount=bc.getBalance(sender);
        if(resAmount<amount){
            System.out.println("余额不足，交易失败");
            return null;
        }
        //遍历账本，找到足够的金额（ 即找到足够的到A中去的output，使这些output中的value和大于需要支付的amount)
        //同时，由于后续需要把output转input，故需要记录找到的output所在的交易的id，以及在output[]中的位置
        List<TransactionOutput> willpay=new ArrayList<TransactionOutput>();//用于存放找到的将要花费的output
        Map<String,List<Integer>> TxidOutputIndex=new HashMap<>(100);
        double valueFound=0;
        A:for(int i=1;i<transactionList.size();i++){//遍历交易  从1开始遍历（不知道为什么会在创建区块链时merkletree里会自己加一个莫名奇妙的交易）
            TransactionOutput []transactionOutputfalse=transactionList.get(i).getTransOutput();
            TransactionOutput []transactionOutput=transactionOutputfalse;
            List<Integer> outputIndex=new ArrayList<>();
            for(int j=0;j<transactionOutput.length;j++){//遍历当前交易中的output
                if(transactionOutput[j].getRecipientAddress()!=sender){//若不是到sender中的output就跳过
                    continue ;
                }
                //是属于sender的比特币
                willpay.add(transactionOutput[j]);
                valueFound+=transactionOutput[j].getValue();
                outputIndex.add(j);
                TxidOutputIndex.put(transactionList.get(i).transactionId,outputIndex);
                if(valueFound>amount){
                    break A;
                }
            }
        }
        //output转input 即建立新的input，使input中的交易id和output索引能和 willpay中的output一 一对应起来
        TransactionInput []transactionInput=new TransactionInput[willpay.size()];
        Set keyset=TxidOutputIndex.keySet();
        Iterator<String> iterator=keyset.iterator();
        int j=0;
        while(iterator.hasNext()){
            String txid=iterator.next();
            List<Integer> index=TxidOutputIndex.get(txid);
            for(int i=0;i<index.size();i++){
                transactionInput[j]=new TransactionInput(txid,index.get(i),sender);
                j++;
            }
        }
        //创建属于收款人的output,即到收款人去的output,最后一笔单独处理，因为可能会有找零
        TransactionOutput []toRecipient=new TransactionOutput[willpay.size()+1];//多一笔找零(到付款人地址去），找零可以是0

        for(int i=0;i<toRecipient.length-2;i++){
            toRecipient[i]=willpay.get(i);
            toRecipient[i].setRecipientAddress(recipient);
        }
        Double changeBack=valueFound-amount;//找零数目
        TransactionOutput lastone=new TransactionOutput(willpay.get(willpay.size()-1).getValue()-changeBack,recipient);
        toRecipient[toRecipient.length-2]=lastone;
        TransactionOutput back=new TransactionOutput(changeBack,sender);
        toRecipient[toRecipient.length-1]=back;
        //返回交易
        Transaction transaction=new Transaction(transactionInput,toRecipient);
        transaction.setTractionId(transaction);
        return transaction;
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

        if (this.transOutput == null) {
            transOut = null;
        } else {
            transOut = this.transOutput.clone();
        }
        return new Transaction(transIn, transOut, this.amount);
    }


}
