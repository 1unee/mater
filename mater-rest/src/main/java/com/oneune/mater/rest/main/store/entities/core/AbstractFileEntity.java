package com.oneune.mater.rest.main.store.entities.core;

import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@ToString
public class AbstractFileEntity extends AbstractEntity {
    String name;
    String type;
    /**
     * In bytes.
     */
    Long size;
    String url;
}
