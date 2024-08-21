package com.oneune.mater.rest.main.store.entities;

import com.oneune.mater.rest.main.store.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "role")
@SequenceGenerator(sequenceName = "role_id_seq", name = "id_seq", allocationSize = 1)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class RoleEntity extends AbstractEntity {
    @Enumerated(EnumType.STRING)
    RoleEnum name;
}
