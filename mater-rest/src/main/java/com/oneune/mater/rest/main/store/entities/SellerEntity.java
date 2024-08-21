package com.oneune.mater.rest.main.store.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "seller")
@SequenceGenerator(sequenceName = "seller_id_seq", name = "id_seq", allocationSize = 1)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class SellerEntity extends AbstractEntity {

    @OneToOne
    @JoinColumn(name = "user_id")
    UserEntity user;

    Float score;

    @OneToMany(
            mappedBy = "seller",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    List<ContactEntity> contacts = new ArrayList<>();

    @OneToMany(
            mappedBy = "seller",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    List<CarEntity> cars = new ArrayList<>();
}
