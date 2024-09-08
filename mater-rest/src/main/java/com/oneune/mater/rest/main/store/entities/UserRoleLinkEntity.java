package com.oneune.mater.rest.main.store.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Entity
@Table(name = "user_role_link")
@SequenceGenerator(sequenceName = "user_role_link_id_seq", name = "id_seq", allocationSize = 1)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class UserRoleLinkEntity extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    UserEntity user;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @ToString.Exclude
    RoleEntity role;

    @Builder.Default
    Instant createdAt = Instant.now();
}
