package com.blockchain.backend.pojo;

import com.blockchain.backend.util.CalculateUtil;
import lombok.Data;
import org.springframework.data.util.Pair;

import java.io.*;

/**
 * @author njuselhx
 */
@Data
public class User {

    public static final String FILEPATH_ROOT = "src/main/resources/accounts/";

    private Integer id;
    private String username;
    private String password;

    /**
     * 比特币钱包
     */
    private BitcoinWallet wallet;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {
    }

    /**
     * 生成新的私钥、公钥和地址并加到钱包中
     */
    public void generateNewKeysAndAddress() {
        if (this.wallet == null) {
            this.wallet = new BitcoinWallet();
        }
        Pair<String, Pair<String, String>> keys = CalculateUtil.generateKeys();
        this.wallet.getPrivateKeys().add(keys.getFirst());
        this.wallet.getPublicKeys().add(keys.getSecond().getFirst() + keys.getSecond().getSecond());
        this.wallet.getBitcoinAddresses().add(CalculateUtil.generateAddress(keys.getSecond()));
    }

    /**
     * 序列化钱包对象
     */
    public void serializeWallet() {
        try {
            FileOutputStream fos = new FileOutputStream(FILEPATH_ROOT + this.username + ".txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.wallet);
            oos.close();
            fos.close();
        } catch (IOException e) {
            System.err.println("serialize error!");
        }
    }

    /**
     * 反序列化钱包对象
     */
    public void deserializeWallet() {
        try {
            FileInputStream fis = new FileInputStream( FILEPATH_ROOT+ this.username + ".txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            this.wallet = (BitcoinWallet) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("deserialize error!");
        }
    }

}
