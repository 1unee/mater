package com.oneune.mater.rest.main.store.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class SellerDto extends AbstractDto {
    Float score;
    @ToString.Exclude
    List<ContactDto> contacts;
    @ToString.Exclude
    List<CarDto> cars;
}

