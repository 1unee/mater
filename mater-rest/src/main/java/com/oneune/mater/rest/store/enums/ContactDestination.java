package com.oneune.mater.rest.store.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Getter
public enum ContactDestination {

    DEVELOPER("Разработчик", "Связаться с разработчиком"),
    SELLER("Продавец", "Связаться с продавцом"),
    SUPPORT("Поддержка", "Связаться с поддержкой");

    String title;
    String description;
}
