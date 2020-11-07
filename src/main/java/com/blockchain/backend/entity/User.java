package com.blockchain.backend.entity;

import com.blockchain.backend.util.CalculateUtil;
import org.springframework.data.util.Pair;

import javax.persistence.*;

/**
 * @author 听取WA声一片
 */
@Entity
@Table(name = "user", schema = "blockchain")
public class User {

    private Integer id;
    private String username;
    private String password;

    /**
     * 比特币钱包
     */
    private final BitcoinWallet wallet;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.wallet = new BitcoinWallet();
        this.generateNewKeysAndAddress();
    }

    public User() {
        throw new RuntimeException("Illegal user!");
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BitcoinWallet getWallet() {
        return this.wallet;
    }

    /**
     * 生成新的私钥、公钥和地址并加到钱包中
     */
    public void generateNewKeysAndAddress() {
        Pair<String, Pair<String, String>> keys = CalculateUtil.generateKeys();
        this.wallet.getPrivateKeys().add(keys.getFirst());
        this.wallet.getPublicKeys().add(keys.getSecond());
        this.wallet.getBitcoinAddresses().add(CalculateUtil.generateAddress(keys.getSecond()));
    }

}
