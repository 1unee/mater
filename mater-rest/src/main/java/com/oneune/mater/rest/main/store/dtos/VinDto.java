package com.oneune.mater.rest.main.store.dtos;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.Year;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@SuperBuilder
@Data
public class VinDto {

    Code code;
    Description description;
    ProductionYear productionYear;
    String serialNumber;

    public static class Code {
        String code;
        String country;
        String factory;
    }

    public static class Description {
        String description;
        String model;
        String bodyType;
    }

    public static class ProductionYear {
        String code;
        Year year;
    }
}
