package com.oneune.mater.rest.main.store.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Deprecated(forRemoval = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Getter
public enum Constants {

    WEB_URL("https://mater-webapp.web.app"),
    URL2("https://google.com");

    String value;
}
