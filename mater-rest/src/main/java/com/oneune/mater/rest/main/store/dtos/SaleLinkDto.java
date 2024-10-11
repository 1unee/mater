package com.oneune.mater.rest.main.store.dtos;

import com.oneune.mater.rest.main.store.enums.SaleStatusEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

/**
 * @see com.oneune.mater.rest.main.store.entities.SaleLinkEntity
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class SaleLinkDto extends AbstractDto {
    UserDto buyer;
    CarDto car;
    Instant createdAt;
    SaleStatusEnum status;
    Float score;
}
