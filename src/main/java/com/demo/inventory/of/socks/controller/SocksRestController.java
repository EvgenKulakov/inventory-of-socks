package com.demo.inventory.of.socks.controller;

import com.demo.inventory.of.socks.service.SocksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/socks")
public class SocksRestController {

    private final SocksService socksService;

    @Autowired
    public SocksRestController(SocksService socksService) {
        this.socksService = socksService;
    }

    @Operation(summary = "Регистрация прихода носков")
    @ApiResponse(responseCode = "200", description = "Income registered successfully")
    @PostMapping("/income")
    public ResponseEntity<String> registerIncome(
            @Parameter(description = "Цвет носков") @RequestParam String color,
            @Parameter(description = "Процентное содержание хлопка") @RequestParam int cottonPercentage,
            @Parameter(description = "Количество") @RequestParam int quantity
    ) {
        socksService.registerIncome(color, cottonPercentage, quantity);
        return ResponseEntity.ok("Income registered successfully");
    }

    @Operation(summary = "Регистрация отпуска носков")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Outcome registered successfully"),
            @ApiResponse(responseCode = "400", description = "Not enough socks in stock")
    })
    @PostMapping("/outcome")
    public ResponseEntity<String> registerOutcome(
            @Parameter(description = "Цвет носков") @RequestParam String color,
            @Parameter(description = "Процентное содержание хлопка") @RequestParam int cottonPercentage,
            @Parameter(description = "Количество") @RequestParam int quantity
    ) {
        boolean success = socksService.registerOutcome(color, cottonPercentage, quantity);
        if (success) {
            return ResponseEntity.ok("Outcome registered successfully");
        } else return ResponseEntity.badRequest().body("Not enough socks in stock");
    }

    @Operation(summary = "Получение общего количества носков с фильтрацией")
    @ApiResponse(responseCode = "200", description = "Quantity socks")
    @GetMapping
    public ResponseEntity<Integer> getSocks(
            @Parameter(description = "Цвет носков") @RequestParam String color,
            @Parameter(description = "Оператор сравнения (moreThan, lessThan, equal)") @RequestParam String operator,
            @Parameter(description = "Процентное содержание хлопка") @RequestParam int cottonPercentage
    ) {
        int quantity = socksService.getSocks(color, operator, cottonPercentage);
        return ResponseEntity.ok(quantity);
    }

    @Operation(summary = "Обновление данных носков")
    @ApiResponse(responseCode = "200", description = "Sock updated successfully")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateSock(
            @Parameter(description = "id") @PathVariable int id,
            @Parameter(description = "Цвет носков") @RequestParam String color,
            @Parameter(description = "Процентное содержание хлопка") @RequestParam int cottonPercentage,
            @Parameter(description = "Количество") @RequestParam int quantity
    ) {
        socksService.updateSock(id, color, cottonPercentage, quantity);
        return ResponseEntity.ok("Sock updated successfully");
    }

    @Operation(summary = "Загрузка партий носков из CSV файла")
    @ApiResponse(responseCode = "200", description = "File csv uploaded successful")
    @PostMapping("/batch")
    public ResponseEntity<String> uploadBatch(
            @Parameter(description = "CSV файл", content = @Content(mediaType = "multipart/form-data"))
            @RequestPart("file") MultipartFile file
    ) {
        socksService.processBatchUpload(file);
        return ResponseEntity.ok("File csv uploaded successful");
    }
}
