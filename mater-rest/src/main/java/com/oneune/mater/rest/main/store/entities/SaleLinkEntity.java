package com.oneune.mater.rest.main.store.entities;

import com.oneune.mater.rest.main.store.entities.core.AbstractEntity;
import com.oneune.mater.rest.main.store.enums.SaleStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Entity
@Table(name = "sale_link")
@SequenceGenerator(sequenceName = "sale_link_id_seq", name = "id_seq", allocationSize = 1)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class SaleLinkEntity extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    UserEntity buyer;

    @ManyToOne
    @JoinColumn(name = "car_id")
    CarEntity car;

    @Builder.Default
    Instant createdAt = Instant.now();

    @Enumerated(EnumType.STRING)
    SaleStatusEnum status;

    @Builder.Default
    Float score = 5.0F;
}
