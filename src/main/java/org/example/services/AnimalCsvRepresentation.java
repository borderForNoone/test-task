package org.example.services;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnimalCsvRepresentation {
    @CsvBindByName(column = "Name")
    private String name;

    @CsvBindByName(column = "Type")
    private String type;

    @CsvBindByName(column = "Sex")
    private String sex;

    @CsvBindByName(column = "Weight")
    private Integer weight;

    @CsvBindByName(column = "Cost")
    private Integer cost;
}
