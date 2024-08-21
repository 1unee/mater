package com.oneune.mater.rest.main.store.entities;

import com.oneune.mater.rest.main.contracts.Identifiable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "\"user\"")
@SequenceGenerator(sequenceName = "user_id_seq", name = "id_seq", allocationSize = 1)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class UserEntity extends AbstractEntity {

    String username;
    String email;
    Long telegramId;
    Instant registeredAt;

    @OneToOne
    @JoinColumn(name = "personal_id")
    PersonalEntity personal;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_role_link",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    List<RoleEntity> roles = new ArrayList<>();

    public static UserEntity create(String username, String email, List<RoleEntity> roles) {
        if (Identifiable.areAllEntitiesPosted(roles)) {
            UserEntity userEntity = new UserEntity();
            userEntity.username = username;
            userEntity.email = email;
            userEntity.roles.addAll(roles);
            return userEntity;
        } else {
            String allNonPostedRoles = Identifiable.findNonPostedEntities(roles).stream()
                    .map(role -> role.getName().name())
                    .collect(Collectors.joining(", "));
            throw new UnsupportedOperationException(
                    "Roles <%s> are not exists (ids not defined)".formatted(allNonPostedRoles)
            );
        }
    }
}
