package com.blockchain.backend.entity;

import javax.persistence.*;

/**
 * @author njuselhx
 */
@Entity
@Table(name = "chain", schema = "blockchain")
public class Chain {

    private int id;
    private long nonce;

    public Chain(long nonce) {
        this.nonce = nonce;
    }

    public Chain() {
    }

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "nonce")
    public long getNonce() {
        return nonce;
    }

    public void setNonce(long nonce) {
        this.nonce = nonce;
    }

}
