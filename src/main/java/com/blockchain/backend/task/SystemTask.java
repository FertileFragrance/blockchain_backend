package com.blockchain.backend.task;

import com.blockchain.backend.dao.ChainMapper;
import com.blockchain.backend.entity.Chain;
import com.blockchain.backend.util.ChainsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author njuselhx
 */
@Component
public class SystemTask {

    @Autowired
    private ChainMapper chainMapper;

    public SystemTask(ChainMapper chainMapper) {
        this.chainMapper = chainMapper;
    }

    /**
     * 每隔10分钟所有链上新增区块
     */
    @Scheduled(cron = "0 */10 * * * ?")
    public void autoAddBlock() {
        List<Chain> chains = (List<Chain>) chainMapper.findAll();
        List<Long> nonces = new ArrayList<>();
        for (Chain chain : chains) {
            nonces.add(chain.getNonce());
        }
        ChainsUtil.deserializeAllChains(nonces);
        ChainsUtil.updateChains();
        for (int i = 0; i < ChainsUtil.getBlockchains().size(); i++) {
            try (FileWriter fw = new FileWriter(ChainsUtil.CHAIN_FILEPATH_ROOT + ChainsUtil
                    .getBlockchains().get(i).getLastBlock().getBlockHead().getNonce() + ".txt", false)) {
                fw.flush();
            } catch (IOException e) {
                System.err.println("rewrite file error!");
            }
        }
        ChainsUtil.serializeAllChains();
    }

    /**
     * 每月1次判断调整难度
     */
    @Scheduled(cron = "0 0 3 1 * *")
    public void adjustDifficulty() {
        int size = ChainsUtil.getBlockchains().size();
        if (99 < size && size <= 999) {
            ChainsUtil.setDifficulty(6);
        } else if (999 < size && size <= 9999) {
            ChainsUtil.setDifficulty(7);
        } else {
            ChainsUtil.setDifficulty(8);
        }
    }

}
