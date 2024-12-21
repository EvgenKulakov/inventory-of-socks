package com.demo.inventory.of.socks.utils;

import com.demo.inventory.of.socks.model.Socks;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

public class CsvParser {

    public static List<Socks> csvToSocksList(MultipartFile file) throws IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            CsvToBean<Socks> csvToBean = new CsvToBeanBuilder<Socks>(reader)
                    .withType(Socks.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return csvToBean.parse();
        }
    }
}
