package com.oneune.mater.rest.main.store.dtos;

import com.oneune.mater.rest.main.store.enums.RoleEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class RoleDto extends AbstractDto {
    RoleEnum name;
}
