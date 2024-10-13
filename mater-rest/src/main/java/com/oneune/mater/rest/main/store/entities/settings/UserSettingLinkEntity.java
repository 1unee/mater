package com.oneune.mater.rest.main.store.entities.settings;

import com.oneune.mater.rest.main.store.entities.core.AbstractAuditableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "user_setting_link")
@SequenceGenerator(sequenceName = "user_setting_link_id_seq", name = "id_seq", allocationSize = 1)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class UserSettingLinkEntity extends AbstractAuditableEntity {

    @ManyToOne
    @JoinColumn(name = "user_settings_id", nullable = false)
    UserSettingsEntity userSettings;

    @ManyToOne
    @JoinColumn(name = "setting_id", nullable = false)
    SettingEntity setting;

    @ManyToOne
    @JoinColumn(name = "selected_option_id", nullable = false)
    OptionEntity selectedOption;
}

