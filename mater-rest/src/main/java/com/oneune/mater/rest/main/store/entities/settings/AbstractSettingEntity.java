package com.oneune.mater.rest.main.store.entities.settings;

import com.oneune.mater.rest.main.store.entities.core.AbstractAuditableEntity;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString
public class AbstractSettingEntity extends AbstractAuditableEntity {
    Integer code;
    String title;
    String value;
    String description;
}
