package com.blockchain.backend.pojo.chain;

import com.blockchain.backend.pojo.chain.block.Block;
import com.blockchain.backend.pojo.chain.block.tree.MerkleTree;
import com.blockchain.backend.pojo.chain.block.tree.transaction.Transaction;
import com.blockchain.backend.pojo.chain.block.tree.transaction.inandout.TransactionInput;
import com.blockchain.backend.pojo.chain.block.tree.transaction.inandout.TransactionOutput;
import com.blockchain.backend.util.ChainsUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理Block的链
 * @author 听取WA声一片
 */
public class BlockChain {

    /**
     * 链中已有的最后一个区块
     */
    private Block lastBlock;

    /**
     * 构造方法，矿工找到随机数时调用
     * @param nonce 随机数
     */
    public BlockChain(int nonce) {
        this.lastBlock = new Block("I'm Genesis Block.", nonce, this);
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
     * @return 区块对象
     */
    public Block getLastBlock() {
        return lastBlock;
    }

    /**
     * 增加交易记录
     * @param transaction 交易
     */
    public void addTransaction(Transaction transaction) {
        this.lastBlock.addTransaction(transaction);
    }

    /**
     *余额查询：未被花费的output的value之和
     * @param adress
     * @return
     */
    public double getBalance(String adress){
        TransactionOutput[] transactionOutput=this.getmyUTXOs(adress);
        double total=0;
        for(int i=0;i<transactionOutput.length;i++){
            total+=transactionOutput[i].getValue();
        }
        return  total;

    }

    /**
     * 找到目前未花费的output
     *
     * @param address
     * @return
     */
    public  TransactionOutput[] getmyUTXOs(String address){
        List<TransactionOutput> unspent=new ArrayList<TransactionOutput>();
        Block lastblock=this.lastBlock;//得到该条链的最新区块，最新区块中的merkletree记录了最完整的交易数据。
        MerkleTree merkleTree=lastblock.getMerkleTree();
        List<Transaction> transactions=merkleTree.getTransactions();
        Map<String,Integer[]> InputToOutput=new HashMap<String,Integer[]>();//里面存放intput对应的output所在的交易和索引，这些是被花费过的output
        //遍历交易
        for(int i=1;i<transactions.size();i++){

            Transaction transaction=transactions.get(i);
            TransactionInput []transactionInput=transaction.getTransInput();
            //遍历交易输入：inputs,每个input 对应一个交易id 和该id交易中的一个output（索引）
            for(int j=0;j<transactionInput.length;j++){
                if(address.equals(transactionInput[j].getSenderAddress())){//产生了该地址的input，说明该input对应的output被花费了
                    if(InputToOutput.get(transactionInput[j])==null){//该交易id中还没有被花费过的output
                        Integer []index={transactionInput[j].getOutput_INDEX()};
                        InputToOutput.put(transactionInput[j].getTransactionID(),index);
                    }
                    else{
                        Integer []index=InputToOutput.get(transactionInput[j]);
                        Integer []newindex=new Integer[index.length+1];
                        for(int k=0;k<newindex.length-1;k++){
                            newindex[k]=index[k];
                        }
                        newindex[newindex.length-1]=transactionInput[j].getOutput_INDEX();
                        InputToOutput.put(transactionInput[j].getTransactionID(),newindex);
                    }
                }
            }

        }
        //遍历交易输出
        for(int i=1;i<transactions.size();i++){
            Transaction transaction=transactions.get(i);
            TransactionOutput transactionOutputs[]=transaction.getTransOutput();
            A:for(int a=0;a<transactionOutputs.length;a++){//a 是当前交易中output的索引
                String txid=transactions.get(i).getTransactionId();//得到当前遍历的交易id
                Integer []index=InputToOutput.get(txid);//看当前交易中有无使用过的output
                if(index==null){//没有被使用过的output
                    if(address.equals(transactionOutputs[a].getRecipientAddress())){//找到了属于address的output
                        unspent.add(transactionOutputs[a]);
                    }
                    continue ;
                }
                for(int b=0;b<index.length;b++){
                    if(index[b]==a){//说明该交易id 中的该output索引表示的output被使用过了，跳过。
                        continue A;
                    }
                }
                if(address.equals(transactionOutputs[a].getRecipientAddress())){//找到了属于address的output
                    unspent.add(transactionOutputs[a]);
                }
            }
        }

        TransactionOutput[] unspentoutput=new TransactionOutput[unspent.size()];
        for(int i=0;i<unspentoutput.length;i++){
            unspentoutput[i]=unspent.get(i);
        }
        return unspentoutput;
    }
}
