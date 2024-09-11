package com.oneune.mater.rest.main.store.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class UserRoleLinkDto extends AbstractDto {
    UserDto user;
    RoleDto role;
    Instant createdAt;
}
