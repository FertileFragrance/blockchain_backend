package com.blockchain.backend.entity;

import com.blockchain.backend.util.CalculateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * merkle树
 * @author 听取WA声一片
 */
public class MerkleTree {
    private List<Transaction> transactionList;
    private String roothash;//根哈希，任何一个交易数据被篡改，都会是根哈希的值发生变化。

    public MerkleTree(List<Transaction>transactions){
        this.transactionList=transactions;
        this.roothash="";
    }

    public void setMerkletree(){
        //把每个交易数据都用sha256转化为hash字符串
        List<String> transaction_Hash=new ArrayList<>();
        for(int i=0;i<=transactionList.size()-1;i++){
            String hash= CalculateUtil.applySha256(transactionList.get(i).toString());
            transaction_Hash.add(hash);
        }
        //循环直到所有的hash值合并成一个，得到根哈希
        while(transaction_Hash.size()>1){
            transaction_Hash=getNewHashList(transaction_Hash);
        }
        //设定根哈希
        this.roothash=transaction_Hash.get(0);

    }

    /**
     *
     * @param hashlist
     * @return 进行过一次合并的hashlist
     */


    private List<String> getNewHashList(List<String> hashlist){

        List<String> newHashList = new ArrayList<String>();
        int index = 0;
        while (index < hashlist.size()) {
            // left
            String left = hashlist.get(index);
            index++;
            // right
            String right = "";
            if (index < hashlist.size()) {
                right = hashlist.get(index);
            }

            String newhash = CalculateUtil.applySha256(left + right);
            newHashList.add(newhash);
            index++;

        }

        return newHashList;

    }

}
