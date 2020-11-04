package com.blockchain.backend.entity;

public class GodBlock {

    /**
     * 币的初始持有者
     */
    private User God;

    /**
     *
     */
    private MerkleTree merkleTree;

    /**
     *
     */
    private String currentHashPointer;

    /**
     * chain名，用以区分各条链
     */
    String chainName;

}
