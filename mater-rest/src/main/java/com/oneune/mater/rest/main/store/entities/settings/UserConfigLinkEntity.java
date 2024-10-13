package com.oneune.mater.rest.main.store.entities.settings;

import com.oneune.mater.rest.main.store.entities.core.AbstractAuditableEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "user_config_link")
@SequenceGenerator(sequenceName = "user_config_link_id_seq", name = "id_seq", allocationSize = 1)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class UserConfigLinkEntity extends AbstractAuditableEntity {

    @ManyToOne
    @JoinColumn(name = "user_settings_id", nullable = false)
    @ToString.Exclude
    UserSettingsEntity userSettings;

    @ManyToOne
    @JoinColumn(name = "config_id", nullable = false)
    @ToString.Exclude
    ConfigEntity config;
}

