package com.blockchain.backend;

import com.blockchain.backend.dao.ChainMapper;
import com.blockchain.backend.dao.UserMapper;
import com.blockchain.backend.pojo.chain.BlockChain;
import com.blockchain.backend.pojo.chain.block.Block;
import com.blockchain.backend.pojo.user.User;
import com.blockchain.backend.util.ChainsUtil;
import com.blockchain.backend.vo.UserVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
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
        BlockChain chain1 = new BlockChain(0, null, null);
        BlockChain chain2 = new BlockChain(1, null, null);
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
        BlockChain blockChain = new BlockChain(256, "333", null);
        System.out.println(blockChain.getLastBlock().getBlockHead().getNonce());
    }

    @Test
    void testTransactionSize() {
        List<Long> nonces = new ArrayList<>();
        nonces.add(596138L);
        ChainsUtil.deserializeAllChains(nonces);
        System.out.println(ChainsUtil.getBlockchains().get(0).getLastBlock().getMerkleTree().getTransactions().size());
    }

    @Test
    void testTransferAccount() {
        UserVO userVO = new UserVO();
        userVO.setUsername("test");
        List<com.blockchain.backend.entity.User> users = userMapper.findByUsername(userVO.getUsername());
        assert users.size() == 1;
        User userPojo = new User();
        BeanUtils.copyProperties(users.get(0), userPojo);
        userPojo.deserializeWallet();
        List<Long> nonces = new ArrayList<>();
        nonces.add(1153627L);
        nonces.add(3911299L);
        ChainsUtil.deserializeAllChains(nonces);
    }

    @Test
    void testTransaction() {
        List<Long> nonces = new ArrayList<>();
        nonces.add(1153627L);
        ChainsUtil.deserializeAllChains(nonces);
        System.out.println(ChainsUtil.getBlockchains().get(0).getLastBlock().getMerkleTree().getTransactions().size());
        for (BlockChain blockChain : ChainsUtil.getBlockchains()) {
            System.out.println(blockChain.getLastBlock().getMerkleTree().getTransactions());
        }
    }

}
