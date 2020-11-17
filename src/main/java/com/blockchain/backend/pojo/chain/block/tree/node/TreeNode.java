package com.blockchain.backend.pojo.chain.block.tree.node;

import lombok.Data;

/**
 * Merkle树的节点
 *
 * @author 听取WA声一片
 */
@Data
public class TreeNode {

    /**
     * 哈希值
     */
    private String hashValue;

    public TreeNode(String hashValue) {
        this.hashValue = hashValue;
    }

    @Override
    public TreeNode clone() {
        return new TreeNode(this.hashValue);
    }

}
