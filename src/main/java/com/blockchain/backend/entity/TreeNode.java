package com.blockchain.backend.entity;

import lombok.Data;

/**
 * Merkle树的节点
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

}
