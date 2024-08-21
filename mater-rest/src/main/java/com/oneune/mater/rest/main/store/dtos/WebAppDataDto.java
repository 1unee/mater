package com.oneune.mater.rest.main.store.dtos;

import com.oneune.mater.rest.main.store.enums.WebAppDataEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
@EqualsAndHashCode
public class WebAppDataDto<D extends Serializable> {
    WebAppDataEnum type;
    D data;
}
