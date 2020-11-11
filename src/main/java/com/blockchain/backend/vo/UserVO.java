package com.blockchain.backend.vo;

import com.blockchain.backend.pojo.BitcoinWallet;
import lombok.Data;

/**
 * @author 听取WA声一片
 */
@Data
public class UserVO {

    private Integer id;
    private String username;
    private String password;

    private BitcoinWallet wallet;

}
