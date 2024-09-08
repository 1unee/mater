package com.oneune.mater.rest.main.store.entities;

import com.oneune.mater.rest.main.store.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "role")
@SequenceGenerator(sequenceName = "role_id_seq", name = "id_seq", allocationSize = 1)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class RoleEntity extends AbstractEntity {

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    @Builder.Default
    List<UserRoleLinkEntity> userRoleLinks = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    RoleEnum name;
}
