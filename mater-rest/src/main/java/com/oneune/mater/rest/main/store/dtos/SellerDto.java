package com.oneune.mater.rest.main.store.dtos;

import com.oneune.mater.rest.main.store.dtos.core.AbstractDto;
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
    List<ContactDto> contacts;
    List<CarDto> cars;
}

