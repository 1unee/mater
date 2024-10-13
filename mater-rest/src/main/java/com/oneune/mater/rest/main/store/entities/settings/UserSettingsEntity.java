package com.oneune.mater.rest.main.store.entities.settings;

import com.oneune.mater.rest.main.store.dtos.settings.UserSettingsDto;
import com.oneune.mater.rest.main.store.entities.UserEntity;
import com.oneune.mater.rest.main.store.entities.core.AbstractAuditableEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @see UserSettingsDto
 */
@Entity
@Table(name = "user_settings")
@SequenceGenerator(sequenceName = "user_settings_id_seq", name = "id_seq", allocationSize = 1)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class UserSettingsEntity extends AbstractAuditableEntity {

    @OneToOne
    @JoinColumn(name = "user_id")
    UserEntity user;

    @OneToMany(mappedBy = "userSettings", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<UserConfigLinkEntity> userConfigLinks = new ArrayList<>();

    @OneToMany(mappedBy = "userSettings", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<UserSettingLinkEntity> userSettingLinks = new ArrayList<>();
}
