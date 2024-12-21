package com.demo.inventory.of.socks;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.demo.inventory.of.socks.controller.SocksRestController;
import com.demo.inventory.of.socks.model.Socks;
import com.demo.inventory.of.socks.service.SocksService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;


@WebMvcTest(SocksRestController.class)
class SocksRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SocksService socksService;

    @Test
    void registerIncomeTest() throws Exception {
        String color = "Red";
        int cottonPercentage = 80;
        int quantity = 100;

        doNothing().when(socksService).registerIncome(color, cottonPercentage, quantity);

        mockMvc.perform(post("/api/socks/income")
                        .param("color", color)
                        .param("cotton_percentage", String.valueOf(cottonPercentage))
                        .param("quantity", String.valueOf(quantity)))
                .andExpect(status().isOk())
                .andExpect(content().string("Income registered successfully"));
    }

    @Test
    void registerOutcomeSuccessTest() throws Exception {
        String color = "Blue";
        int cottonPercentage = 90;
        int quantity = 50;

        when(socksService.registerOutcome(color, cottonPercentage, quantity)).thenReturn(true);

        mockMvc.perform(post("/api/socks/outcome")
                        .param("color", color)
                        .param("cotton_percentage", String.valueOf(cottonPercentage))
                        .param("quantity", String.valueOf(quantity)))
                .andExpect(status().isOk())
                .andExpect(content().string("Outcome registered successfully"));
    }

    @Test
    void registerOutcomeFailureTest() throws Exception {
        String color = "Green";
        int cottonPercentage = 75;
        int quantity = 200;

        when(socksService.registerOutcome(color, cottonPercentage, quantity)).thenReturn(false);

        mockMvc.perform(post("/api/socks/outcome")
                        .param("color", color)
                        .param("cotton_percentage", String.valueOf(cottonPercentage))
                        .param("quantity", String.valueOf(quantity)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Not enough socks in stock"));
    }

    @Test
    void getSocksTest() throws Exception {
        String color = "Black";
        String operator = "moreThan";
        int cottonPercentage = 85;
        int quantity = 100;

        when(socksService.getSocks(color, operator, cottonPercentage)).thenReturn(quantity);

        mockMvc.perform(get("/api/socks")
                        .param("color", color)
                        .param("operator", operator)
                        .param("cotton_percentage", String.valueOf(cottonPercentage)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(quantity));
    }

    @Test
    void updateSockTest() throws Exception {
        int id = 1;
        String color = "Yellow";
        int cottonPercentage = 60;
        int quantity = 500;

        doNothing().when(socksService).updateSock(id, color, cottonPercentage, quantity);

        mockMvc.perform(put("/api/socks/{id}", id)
                        .param("color", color)
                        .param("cotton_percentage", String.valueOf(cottonPercentage))
                        .param("quantity", String.valueOf(quantity)))
                .andExpect(status().isOk())
                .andExpect(content().string("Sock updated successfully"));
    }

    @Test
    void uploadBatchTest() throws Exception {
        String fileName = "test.csv";
        MockMultipartFile file = new MockMultipartFile("file", fileName, "text/csv", "some csv content".getBytes());

        doNothing().when(socksService).processBatchUpload(file);

        mockMvc.perform(multipart("/api/socks/batch")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("File csv uploaded successful"));
    }

    @Test
    public void testGetSocksByRangeWithSorting_Success() throws Exception {
        List<Socks> mockSocks = List.of(
                new Socks(1, "red", 40, 10),
                new Socks(2, "red", 50, 5)
        );

        when(socksService.getSocksByRangeWithSorting(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(mockSocks);

        mockMvc.perform(get("/api/socks/range")
                        .param("cotton_percentage_start", "30")
                        .param("cotton_percentage_end", "70")
                        .param("sort_by", "cotton_percentage")
                        .param("order", "desc")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].color").value("red"))
                .andExpect(jsonPath("$[0].cotton_percentage").value(40))
                .andExpect(jsonPath("$[0].quantity").value(10))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].color").value("red"))
                .andExpect(jsonPath("$[1].cotton_percentage").value(50))
                .andExpect(jsonPath("$[1].quantity").value(5));

        verify(socksService, times(1))
                .getSocksByRangeWithSorting(30, 70, "cotton_percentage", "desc");
    }

    @Test
    public void testGetSocksByRangeWithSorting_InvalidSortBy() throws Exception {
        mockMvc.perform(get("/range")
                        .param("cotton_percentage_start", "30")
                        .param("cotton_percentage_end", "70")
                        .param("sort_by", "invalid_column")
                        .param("order", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetSocksByRangeWithSorting_InvalidOrder() throws Exception {
        mockMvc.perform(get("/range")
                        .param("color", "red")
                        .param("cotton_percentage_start", "30")
                        .param("cotton_percentage_end", "70")
                        .param("sort_by", "cotton_percentage")
                        .param("order", "invalid_order")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetSocksByRangeWithSorting_NoResults() throws Exception {
        when(socksService.getSocksByRangeWithSorting(30, 70, "cotton_percentage", "asc"))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/range")
                        .param("cotton_percentage_start", "30")
                        .param("cotton_percentage_end", "70")
                        .param("sort_by", "cotton_percentage")
                        .param("order", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(socksService, times(0))
                .getSocksByRangeWithSorting(30, 70, "cotton_percentage", "asc");
    }
}