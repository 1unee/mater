package com.oneune.mater.rest.main.store.entities;

import com.oneune.mater.rest.main.store.entities.core.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @see com.oneune.mater.rest.main.store.dtos.CarDto
 */
@Entity
@Table(name = "car")
@SequenceGenerator(sequenceName = "car_id_seq", name = "id_seq", allocationSize = 1)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class CarEntity extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "seller_id")
    SellerEntity seller;

    /**
     * Марка.
     */
    String brand;

    /**
     * Модель.
     */
    String model;

    /**
     * Год выпуска (положительное число).
     */
    Integer productionYear;

    /**
     * Цена (от 0 до 1 000 000 000.00 в рублях).
     */
    BigDecimal price;

    /**
     * Пробег (положительное число в км).
     */
    BigDecimal mileage;

    /**
     * VIN (vehicle identification number) - уникальный идентификационный номер
     * (по официальной документации состоит из 17 символов).
     */
    @Column(name = "vehicle_identification_number")
    String VIN;

    /**
     * Количество владельцев.
     */
    Integer ownersAmount;

    /**
     * Мощность в л.с.
     */
    Integer power;

    /**
     * Прикрепленные файлы.
     */
    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    List<CarFileEntity> files = new ArrayList<>();
}
