package com.oneune.mater.rest.store.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Getter
public enum UpdateType {

    COMMAND("/", "Команда"),
    UNKNOWN("", "Неизвестно");

    String prefix;
    String description;
}
