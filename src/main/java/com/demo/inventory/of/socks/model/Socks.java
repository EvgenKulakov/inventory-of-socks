package com.demo.inventory.of.socks.model;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Socks {
    private Integer id;
    @CsvBindByName(column = "color")
    private String color;
    @CsvBindByName(column = "cotton_percentage")
    private int cottonPercentage;
    @CsvBindByName(column = "quantity")
    private int quantity;
}
