package com.demo.inventory.of.socks;

import com.demo.inventory.of.socks.dao.SocksDAO;
import com.demo.inventory.of.socks.model.Socks;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SocksDAOTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private SocksDAO socksDAO;

    public SocksDAOTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIncreaseSocksStock() {
        String color = "red";
        int cottonPercentage = 50;
        int quantity = 10;

        when(jdbcTemplate.update(anyString(), eq(color), eq(cottonPercentage), eq(quantity), eq(quantity)))
                .thenReturn(1);

        socksDAO.increaseSocksStock(color, cottonPercentage, quantity);

        verify(jdbcTemplate, times(1)).update(
                anyString(), eq(color), eq(cottonPercentage), eq(quantity), eq(quantity));
    }

    @Test
    void testDecreaseSocksStock_SufficientQuantity() {
        String color = "red";
        int cottonPercentage = 50;
        int quantity = 5;

        when(jdbcTemplate.query(anyString(), (ResultSetExtractor<Object>) any(), eq(color), eq(cottonPercentage)))
                .thenAnswer(invocation -> {
                    ResultSetExtractor<Integer> rse = invocation.getArgument(1);
                    ResultSet rs = mock(ResultSet.class);
                    when(rs.next()).thenReturn(true);
                    when(rs.getInt("quantity")).thenReturn(10);
                    return rse.extractData(rs);
                });

        when(jdbcTemplate.update(anyString(), eq(quantity), eq(color), eq(cottonPercentage))).thenReturn(1);

        boolean result = socksDAO.decreaseSocksStock(color, cottonPercentage, quantity);

        assertTrue(result);
        verify(jdbcTemplate, times(1)).update(anyString(), eq(quantity), eq(color), eq(cottonPercentage));
    }

    @Test
    void testDecreaseSocksStock_InsufficientQuantity() {
        String color = "red";
        int cottonPercentage = 50;
        int quantity = 10;

        when(jdbcTemplate.query(anyString(), (ResultSetExtractor<Object>) any(), eq(color), eq(cottonPercentage)))
                .thenAnswer(invocation -> {
                    ResultSetExtractor<Integer> rse = invocation.getArgument(1);
                    ResultSet rs = mock(ResultSet.class);
                    when(rs.next()).thenReturn(true);
                    when(rs.getInt("quantity")).thenReturn(5);
                    return rse.extractData(rs);
                });

        boolean result = socksDAO.decreaseSocksStock(color, cottonPercentage, quantity);

        assertFalse(result);
        verify(jdbcTemplate, never()).update(anyString(), anyInt(), anyString(), anyInt());
    }

    @Test
    void testGetSocksQuantity() {
        String color = "blue";
        int cottonPercentage = 60;
        String operator = "moreThan";

        when(jdbcTemplate.query(anyString(), (ResultSetExtractor<Object>) any(), eq(color), eq(cottonPercentage)))
                .thenAnswer(invocation -> {
                    ResultSetExtractor<Integer> rse = invocation.getArgument(1);
                    ResultSet rs = mock(ResultSet.class);
                    when(rs.next()).thenReturn(true);
                    when(rs.getInt("quantity")).thenReturn(15);
                    return rse.extractData(rs);
                });

        int result = socksDAO.getSocksQuantity(color, operator, cottonPercentage);

        assertEquals(15, result);
    }

    @Test
    void testUpdateSock() {
        int id = 1;
        String color = "green";
        int cottonPercentage = 40;
        int quantity = 20;

        when(jdbcTemplate.update(anyString(), eq(color), eq(cottonPercentage), eq(quantity), eq(id)))
                .thenReturn(1);

        socksDAO.updateSock(id, color, cottonPercentage, quantity);

        verify(jdbcTemplate, times(1))
                .update(anyString(), eq(color), eq(cottonPercentage), eq(quantity), eq(id));
    }

    @Test
    void testBatchInsertSocks() {
        Socks sock = new Socks(null, "red", 50, 10);
        List<Socks> socksBatch = Collections.singletonList(sock);

        when(jdbcTemplate.batchUpdate(anyString(), any(BatchPreparedStatementSetter.class))).thenReturn(new int[]{1});

        socksDAO.batchInsertSocks(socksBatch);

        verify(jdbcTemplate, times(1))
                .batchUpdate(anyString(), any(BatchPreparedStatementSetter.class));
    }
}
