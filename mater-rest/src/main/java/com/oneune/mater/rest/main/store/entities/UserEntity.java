package com.oneune.mater.rest.main.store.entities;

import com.oneune.mater.rest.main.store.entities.core.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * @see com.oneune.mater.rest.main.store.dtos.UserDto
 */
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
    boolean isEmailSet;

    Long telegramId;
    Long telegramChatId;
    Instant registeredAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "personal_id")
    PersonalEntity personal;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "seller_id")
    SellerEntity seller;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    List<UserRoleLinkEntity> userRoleLinks = new ArrayList<>();
}
