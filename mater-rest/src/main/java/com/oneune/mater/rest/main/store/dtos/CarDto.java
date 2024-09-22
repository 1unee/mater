package com.oneune.mater.rest.main.store.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

/**
 * @see com.oneune.mater.rest.main.store.entities.CarEntity
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class CarDto extends AbstractDto {
    String brand;
    String model;
    Integer productionYear;
    BigDecimal price;
    BigDecimal mileage;
    @JsonProperty("VIN")
    String VIN;
    Integer ownersAmount;
    Integer power;
    List<FileDto> files;
}
