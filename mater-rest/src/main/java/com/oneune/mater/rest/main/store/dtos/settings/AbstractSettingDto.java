package com.oneune.mater.rest.main.store.dtos.settings;

import com.oneune.mater.rest.main.store.dtos.core.AbstractAuditableDto;
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
public abstract class AbstractSettingDto extends AbstractAuditableDto {
    Integer code;
    String title;
    String value;
    String description;
}
