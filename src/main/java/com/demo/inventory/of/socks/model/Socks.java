package com.demo.inventory.of.socks.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Socks {
    private int id;
    @CsvBindByName(column = "color")
    private String color;
    @CsvBindByName(column = "cotton_percentage")
    private int cottonPercentage;
    @CsvBindByName(column = "quantity")
    private int quantity;
}
