package com.oneune.mater.rest.main.store.entities;

import com.oneune.mater.rest.main.store.entities.core.FileEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "photo")
@SequenceGenerator(sequenceName = "photo_id_seq", name = "id_seq", allocationSize = 1)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class PhotoEntity extends FileEntity {

    @ManyToOne
    @JoinColumn(name = "car_id")
    CarEntity car;
}
