package com.oneune.mater.rest.main.store.entities.settings;

import com.oneune.mater.rest.main.store.dtos.settings.SettingDto;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @see SettingDto
 */
@Entity
@Table(name = "setting")
@SequenceGenerator(sequenceName = "setting_id_seq", name = "id_seq", allocationSize = 1)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class SettingEntity extends AbstractSettingEntity {

    @OneToMany(mappedBy = "root", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    List<OptionEntity> options = new ArrayList<>();
}
