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
     * 私钥
     */
    private final String privateKey;

    /**
     * 公钥，由私钥生成
     */
    private final Pair<String, String> publicKey;

    /**
     * 比特币地址，由公钥生成
     */
    private final String bitcoinAddress;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        Pair<String, Pair<String, String>> keys = CalculateUtil.generateKeys();
        this.privateKey = keys.getFirst();
        this.publicKey = keys.getSecond();
        this.bitcoinAddress = CalculateUtil.generateAddress(this.publicKey);
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

}
