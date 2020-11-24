package com.blockchain.backend;

import com.blockchain.backend.dao.ChainMapper;
import com.blockchain.backend.dao.UserMapper;
import com.blockchain.backend.pojo.chain.BlockChain;
import com.blockchain.backend.pojo.chain.block.Block;
import com.blockchain.backend.util.ChainsUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class BackendApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ChainMapper chainMapper;

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
        System.out.println(ChainsUtil.getBlockchains().size());
        assert block11.getCurrentBlockHashPointer().equals(block12.getBlockHead().getPreviousBlockHashPointer());
        assert block21.getCurrentBlockHashPointer().equals(block22.getBlockHead().getPreviousBlockHashPointer());
    }

    @Test
    void testNewChain() {
        BlockChain blockChain = new BlockChain(256, "333");
        System.out.println(blockChain.getLastBlock().getBlockHead().getNonce());
    }

    @Test
    void testTransactionSize() {
        List<Long> nonces = new ArrayList<>();
        nonces.add(596138L);
        ChainsUtil.deserializeAllChains(nonces);
        System.out.println(ChainsUtil.getBlockchains().get(0).getLastBlock().getMerkleTree().getTransactions().size());
    }

}
