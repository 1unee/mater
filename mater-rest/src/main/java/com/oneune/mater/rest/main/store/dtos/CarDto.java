package com.oneune.mater.rest.main.store.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CarDto extends AbstractDto {
    SellerDto seller;
    String brand;
    String model;
    Year productionYear;
    BigDecimal price;
    BigDecimal mileage;
    String VIN;
    Integer ownersAmount;
    Integer power;
    List<PhotoDto> photos;
    List<VideoDto> videos;
}
