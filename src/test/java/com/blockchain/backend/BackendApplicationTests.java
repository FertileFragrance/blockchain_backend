package com.blockchain.backend;

import com.blockchain.backend.entity.Block;
import com.blockchain.backend.util.ChainUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void hashPointer() {
        Block block1 = ChainUtil.addToChain(123);
        Block block2 = ChainUtil.addToChain(36975);
        System.out.println(block1.getBlockHead().getPreviousBlockHashPointer());
        System.out.println(block1.getCurrentBlockHashPointer());
        System.out.println(block2.getBlockHead().getPreviousBlockHashPointer());
        System.out.println(block2.getCurrentBlockHashPointer());
        assert block1.getCurrentBlockHashPointer().equals(block2.getBlockHead().getPreviousBlockHashPointer());
    }

}
