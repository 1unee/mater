package com.oneune.mater.rest.main.store.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Getter
public enum UpdateTypeEnum {

    COMMAND("/", "Команда"),
    WEB_APP_DATA("WebAppDataDto", "Данные из веб-приложения"),
    UNKNOWN("", "Неизвестно");

    String prefix;
    String description;
}
