package com.oneune.mater.rest.main.store.entities;

import com.oneune.mater.rest.main.store.entities.core.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Entity
@Table(name = "user_favorite_car_link")
@SequenceGenerator(sequenceName = "user_favorite_car_link_id_seq", name = "id_seq", allocationSize = 1)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class UserFavoriteCarLinkEntity extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity user;

    @ManyToOne
    @JoinColumn(name = "car_id")
    CarEntity car;

    @Builder.Default
    Instant createdAt = Instant.now();
}
