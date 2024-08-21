package com.oneune.mater.rest.main.store.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * USER < SELLER < SUPPORT < ADMIN
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Getter
public enum RoleEnum {

    USER("Покупатель"),
    SELLER("Продавец"),
    SUPPORT("Поддержка"),
    ADMIN("Администратор");

    String rus;
}
