package com.demo.inventory.of.socks;

import com.demo.inventory.of.socks.dao.SocksDAO;
import com.demo.inventory.of.socks.model.Socks;
import com.demo.inventory.of.socks.service.SocksService;
import com.demo.inventory.of.socks.utils.CsvParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SocksServiceTest {

    @Mock
    private SocksDAO socksDAO;

    @InjectMocks
    private SocksService socksService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterIncome() {
        String color = "red";
        int cottonPercentage = 80;
        int quantity = 10;

        socksService.registerIncome(color, cottonPercentage, quantity);
        verify(socksDAO, times(1)).increaseSocksStock(color, cottonPercentage, quantity);
    }

    @Test
    void testRegisterOutcome_Success() {
        String color = "blue";
        int cottonPercentage = 90;
        int quantity = 5;
        when(socksDAO.decreaseSocksStock(color, cottonPercentage, quantity)).thenReturn(true);

        boolean result = socksService.registerOutcome(color, cottonPercentage, quantity);

        assertTrue(result);
        verify(socksDAO, times(1)).decreaseSocksStock(color, cottonPercentage, quantity);
    }

    @Test
    void testRegisterOutcome_Failure() {
        String color = "green";
        int cottonPercentage = 75;
        int quantity = 20;
        when(socksDAO.decreaseSocksStock(color, cottonPercentage, quantity)).thenReturn(false);

        boolean result = socksService.registerOutcome(color, cottonPercentage, quantity);

        assertFalse(result);
        verify(socksDAO, times(1)).decreaseSocksStock(color, cottonPercentage, quantity);
    }

    @Test
    void testGetSocks() {
        String color = "yellow";
        String operator = "moreThen";
        int cottonPercentage = 85;
        int expectedQuantity = 15;
        when(socksDAO.getSocksQuantity(color, operator, cottonPercentage)).thenReturn(expectedQuantity);

        int actualQuantity = socksService.getSocks(color, operator, cottonPercentage);

        assertEquals(expectedQuantity, actualQuantity);
        verify(socksDAO, times(1)).getSocksQuantity(color, operator, cottonPercentage);
    }

    @Test
    void testUpdateSock() {
        int id = 1;
        String color = "black";
        int cottonPercentage = 50;
        int quantity = 100;

        socksService.updateSock(id, color, cottonPercentage, quantity);
        verify(socksDAO, times(1)).updateSock(id, color, cottonPercentage, quantity);
    }
}