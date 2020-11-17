package com.blockchain.backend.pojo.chain.block.tree;

import com.blockchain.backend.pojo.chain.block.tree.node.TreeNode;
import com.blockchain.backend.pojo.chain.block.tree.transaction.Transaction;
import com.blockchain.backend.util.CalculateUtil;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * merkle树
 *
 * @author 听取WA声一片
 */
@Getter
public class MerkleTree {

    /**
     * 根哈希
     */
    private TreeNode merkleRoot;

    /**
     * 树中的交易数据
     */
    private final List<Transaction> transactions;

    /**
     * 树的高度（暂时用不到）
     */
    private int treeHeight;

    /**
     * 创世区块产生时调用此构造方法
     */
    public MerkleTree() {
        Transaction coinBase = new Transaction();
        this.transactions = new ArrayList<>();
        this.transactions.add(coinBase);
        this.merkleRoot = new TreeNode(CalculateUtil.applySha256(coinBase.generateHash()));
        this.treeHeight = 1;
    }

    /**
     * 复制时调用此构造方法
     *
     * @param merkleRoot   上一个的根哈希
     * @param transactions 上一个的交易集合
     * @param treeHeight   上一个的高度
     */
    public MerkleTree(TreeNode merkleRoot, List<Transaction> transactions, int treeHeight) {
        this.merkleRoot = merkleRoot;
        this.transactions = transactions;
        this.treeHeight = treeHeight;
    }

    /**
     * 新增交易记录
     *
     * @param transaction 新增的交易
     */
    public void insertTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        this.updateTreeHeight();
        TreeNode[] treeNodes = new TreeNode[this.transactions.size()];
        for (int i = 0; i < treeNodes.length; i++) {
            treeNodes[i] = new TreeNode(CalculateUtil.applySha256(this.transactions.get(i).generateHash()));
        }
        this.buildTree(treeNodes);
    }

    /**
     * 更新树的高度
     */
    private void updateTreeHeight() {
        for (int k = -1; ; k++) {
            if (Math.pow(2, k) < this.treeHeight && this.treeHeight <= Math.pow(2, k + 1)) {
                this.treeHeight = k + 2;
                return;
            }
        }
    }

    /**
     * 递归构建树
     *
     * @param treeNodes 树的节点
     */
    private void buildTree(TreeNode[] treeNodes) {
        if (treeNodes.length == 1) {
            this.merkleRoot = treeNodes[0];
            return;
        }
        int len = (treeNodes.length + 1) / 2;
        TreeNode[] newNodes = new TreeNode[len];
        if ((treeNodes.length & 1) == 0) {
            for (int i = 0; i < newNodes.length; i++) {
                newNodes[i] = new TreeNode(CalculateUtil.applySha256
                        (treeNodes[2 * i].getHashValue() + treeNodes[2 * i + 1].getHashValue()));
            }
        } else {
            for (int i = 0; i < newNodes.length - 1; i++) {
                newNodes[i] = new TreeNode(CalculateUtil.applySha256
                        (treeNodes[2 * i].getHashValue() + treeNodes[2 * i + 1].getHashValue()));
            }
            newNodes[newNodes.length - 1] = new TreeNode(CalculateUtil
                    .applySha256(treeNodes[treeNodes.length - 1].getHashValue()));
        }
        this.buildTree(newNodes);
    }

    /**
     * 拷贝前一个Merkle树
     *
     * @return 新的Merkle树
     */
    @Override
    public MerkleTree clone() {
        TreeNode treeNode = this.merkleRoot.clone();
        List<Transaction> trades = new ArrayList<>();
        for (Transaction transaction : this.transactions) {
            Transaction trade = transaction.clone();
            trades.add(trade);
        }
        return new MerkleTree(treeNode, trades, this.treeHeight);
    }

}
