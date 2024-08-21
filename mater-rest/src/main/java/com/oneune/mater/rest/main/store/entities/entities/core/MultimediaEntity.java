package com.oneune.mater.rest.main.store.entities.entities.core;

import com.oneune.mater.rest.main.store.entities.AbstractEntity;
import jakarta.persistence.Embeddable;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Embeddable
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@ToString
public class MultimediaEntity extends AbstractEntity {
    byte[] bytes;
}
