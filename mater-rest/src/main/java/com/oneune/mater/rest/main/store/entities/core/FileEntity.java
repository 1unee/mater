package com.oneune.mater.rest.main.store.entities.core;

import com.oneune.mater.rest.main.store.entities.AbstractEntity;
import jakarta.persistence.Column;
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
public abstract class FileEntity extends AbstractEntity {
    String name;
    String type;
    @Column(columnDefinition="text")
    String base64;
}
