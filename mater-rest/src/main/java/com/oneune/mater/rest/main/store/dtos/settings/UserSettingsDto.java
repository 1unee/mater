package com.oneune.mater.rest.main.store.dtos.settings;

import com.oneune.mater.rest.main.store.dtos.core.AbstractAuditableDto;
import com.oneune.mater.rest.main.store.entities.settings.UserSettingsEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @see UserSettingsEntity
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class UserSettingsDto extends AbstractAuditableDto {
    List<ConfigDto> configs;
    List<SettingDto> settings;
}
