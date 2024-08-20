package com.oneune.mater.rest.store.dtos.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Getter
public enum Constants {

    URL("https://mater-webapp.web.app"),
    URL2("https://google.com");

    String field;
}
