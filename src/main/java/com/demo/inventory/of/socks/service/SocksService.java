package com.demo.inventory.of.socks.service;

import com.demo.inventory.of.socks.dao.SocksDAO;
import com.demo.inventory.of.socks.model.Socks;
import com.demo.inventory.of.socks.utils.CsvParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class SocksService {

    private final SocksDAO socksDAO;

    @Autowired
    public SocksService(SocksDAO socksDAO) {
        this.socksDAO = socksDAO;
    }

    @Transactional
    public void registerIncome(String color, int cottonPercentage, int quantity) {
        socksDAO.increaseSocksStock(color, cottonPercentage, quantity);
        log.info("Income: {} socks, color {}, cotton content {}%.", quantity, color, cottonPercentage);
    }

    @Transactional
    public boolean registerOutcome(String color, int cottonPercentage, int quantity) {
        boolean enoughQuantity = socksDAO.decreaseSocksStock(color, cottonPercentage, quantity);
        if (enoughQuantity) {
            log.info("Outcome: {} socks, color {}, cotton content {}%.", quantity, color, cottonPercentage);
        } else {
            log.info("Not enough to reduce by {} socks.", quantity);
        }
        return enoughQuantity;
    }

    @Transactional
    public int getSocks(String color, String operator, int cottonPercentage) {
        int totalQuantity = socksDAO.getSocksQuantity(color, operator, cottonPercentage);
        log.info("Socks total quantity: {} (if color is {} and cotton percentage {} {}%)",
                totalQuantity, color, operator, cottonPercentage);
        return totalQuantity;
    }

    @Transactional
    public void updateSock(int id, String color, int cottonPercentage, int quantity) {
        socksDAO.updateSock(id, color, cottonPercentage, quantity);
        log.info("Update socks by id {}: color - {}, cotton percentage - {}, quantity - {}%.",
                id, color, cottonPercentage, quantity);
    }

    @Transactional
    public void processBatchUpload(MultipartFile file) throws IOException {
        List<Socks> socksBatch = CsvParser.csvToSocksList(file);
        socksDAO.batchInsertSocks(socksBatch);
        log.info("File csv uploaded successful.");
    }
}
