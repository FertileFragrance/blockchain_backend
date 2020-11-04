package com.blockchain.backend.entity;

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

    private String privateKey;
    private String publicKey;
    // private String


    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {
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
