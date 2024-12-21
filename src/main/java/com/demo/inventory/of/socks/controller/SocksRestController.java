package com.demo.inventory.of.socks.controller;

import com.demo.inventory.of.socks.service.SocksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/socks")
public class SocksRestController {

    @Autowired
    private SocksService socksService;

    @PostMapping("/income")
    public ResponseEntity<String> registerIncome(@RequestParam String color,
                                                 @RequestParam int cottonPercentage,
                                                 @RequestParam int quantity) {
        socksService.registerIncome(color, cottonPercentage, quantity);
        return ResponseEntity.ok("Income registered successfully");
    }

    @PostMapping("/outcome")
    public ResponseEntity<String> registerOutcome(@RequestParam String color,
                                                  @RequestParam int cottonPercentage,
                                                  @RequestParam int quantity) {
        boolean success = socksService.registerOutcome(color, cottonPercentage, quantity);
        if (success) {
            return ResponseEntity.ok("Outcome registered successfully");
        } else return ResponseEntity.badRequest().body("Not enough socks in stock");
    }

    @GetMapping
    public ResponseEntity<Integer> getSocks(@RequestParam String color,
                                            @RequestParam String operator,
                                            @RequestParam int cottonPercentage) {
        int quantity = socksService.getSocks(color, operator, cottonPercentage);
        return ResponseEntity.ok(quantity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateSock(@PathVariable int id,
                                             @RequestParam String color,
                                             @RequestParam int cottonPercentage,
                                             @RequestParam int quantity) {
        socksService.updateSock(id, color, cottonPercentage, quantity);
        return ResponseEntity.ok("Sock updated successfully");
    }

    @PostMapping("/batch")
    public ResponseEntity<String> uploadBatch(@RequestParam("file") MultipartFile file) {
        socksService.processBatchUpload(file);
        return ResponseEntity.ok("File csv uploaded successful");
    }
}
