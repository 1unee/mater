package com.oneune.mater.rest.main.store.entities.settings;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "option")
@SequenceGenerator(sequenceName = "option_id_seq", name = "id_seq", allocationSize = 1)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class OptionEntity extends AbstractSettingEntity {

    @ManyToOne
    @JoinColumn(name = "root_id")
    SettingEntity root;
}
