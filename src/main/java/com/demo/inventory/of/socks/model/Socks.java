package com.demo.inventory.of.socks.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Socks {
    private int id;
    private String color;
    private int cottonPercentage;
    private int quantity;
}
