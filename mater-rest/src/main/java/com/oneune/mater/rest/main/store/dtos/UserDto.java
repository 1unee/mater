package com.oneune.mater.rest.main.store.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oneune.mater.rest.main.store.dtos.core.AbstractDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.List;

/**
 * @see com.oneune.mater.rest.main.store.entities.UserEntity
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class UserDto extends AbstractDto {
    String username;
    @JsonProperty("isUsernameSet")
    boolean isUsernameSet;
    String email;
    @JsonProperty("isEmailSet")
    boolean isEmailSet;
    PersonalDto personal;
    SellerDto seller;
    boolean registeredByTelegram;
    Long telegramId;
    Long telegramChatId;
    Instant registeredAt;
    List<RoleDto> roles;
}
