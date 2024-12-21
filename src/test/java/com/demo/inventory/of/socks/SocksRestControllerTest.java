package com.demo.inventory.of.socks;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.demo.inventory.of.socks.controller.SocksRestController;
import com.demo.inventory.of.socks.service.SocksService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;


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
}