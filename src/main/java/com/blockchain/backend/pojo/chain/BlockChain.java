package com.blockchain.backend.pojo.chain;

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
    private Transaction[] getMyUTXOs(String address) {
        /*
        List<TransactionOutput> unspent = new ArrayList<>();
        Block last = this.lastBlock;
        MerkleTree merkleTree = last.getMerkleTree();
        List<Transaction> transactions = merkleTree.getTransactions();
        Map<String, Integer[]> inputToOutput = new HashMap<>(128);
        for (int i = 1; i < transactions.size(); i++) {
            Transaction transaction = transactions.get(i);
            TransactionInput[] transactionInput = transaction.getTransInput();
            for (TransactionInput input : transactionInput) {
                if (address.equals(input.getSenderAddress())) {
                    if (null == inputToOutput.get(input.getTransactionId())) {
                        int index = input.getOutputIndex();
                        inputToOutput.put(input.getTransactionId(), new Integer[index]);
                    } else {
                        Integer[] index = inputToOutput.get(input.getTransactionId());
                        Integer[] newIndex = Arrays.copyOf(index, index.length);
                        newIndex[newIndex.length - 1] = input.getOutputIndex();
                        inputToOutput.put(input.getTransactionId(), newIndex);
                    }
                }
            }
        }
        for (int i = 1; i < transactions.size(); i++) {
            Transaction transaction = transactions.get(i);
            TransactionOutput[] transactionOutputs = transaction.getTransOutput();
            A:
            for (int a = 0; a < transactionOutputs.length; a++) {
                String txId = transactions.get(i).getTransactionId();
                Integer[] index = inputToOutput.get(txId);
                if (index == null) {
                    if (address.equals(transactionOutputs[a].getRecipientAddress())) {
                        unspent.add(transactionOutputs[a]);
                    }
                    continue;
                }
                for (Integer integer : index) {
                    if (integer == a) {
                        continue A;
                    }
                }
                if (address.equals(transactionOutputs[a].getRecipientAddress())) {
                    unspent.add(transactionOutputs[a]);
                }
            }
        }
        TransactionOutput[] unspentOutputs = new TransactionOutput[unspent.size()];
        for (int i = 0; i < unspentOutputs.length; i++) {
            unspentOutputs[i] = unspent.get(i);
        }
        return unspentOutputs;
        */
        return null;
    }


}
