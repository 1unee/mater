package com.oneune.mater.rest.main.store.entities;

import com.oneune.mater.rest.main.store.entities.core.AbstractAuditableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Random;

/**
 * @see com.oneune.mater.rest.main.store.dtos.UserTokenDto
 *
 * Used for join data to user from telegram.
 */
@Entity
@Table(name = "user_token")
@SequenceGenerator(sequenceName = "user_token_id_seq", name = "id_seq", allocationSize = 1)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class UserTokenEntity extends AbstractAuditableEntity {

    private final static Random RANDOM = new Random();

    @OneToOne
    @JoinColumn(name = "user_id")
    UserEntity user;

    Integer value;

    public static Integer getUniqueValue() {
        return RANDOM.nextInt(900000) + 100000;
    }
}
