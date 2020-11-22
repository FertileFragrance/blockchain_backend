package com.blockchain.backend;

import com.blockchain.backend.pojo.chain.BlockChain;
import com.blockchain.backend.pojo.chain.block.Block;
import com.blockchain.backend.util.ChainsUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() {
        BlockChain chain1 = new BlockChain(0, null);
        BlockChain chain2 = new BlockChain(1, null);
        ChainsUtil.updateChains();
        Block block11 = chain1.getLastBlock();
        Block block21 = chain2.getLastBlock();
        ChainsUtil.updateChains();
        Block block12 = chain1.getLastBlock();
        Block block22 = chain2.getLastBlock();
        assert ChainsUtil.getBlockchains().size() == 2;
        assert block11.getCurrentBlockHashPointer().equals(block12.getBlockHead().getPreviousBlockHashPointer());
        assert block21.getCurrentBlockHashPointer().equals(block22.getBlockHead().getPreviousBlockHashPointer());
    }

}
