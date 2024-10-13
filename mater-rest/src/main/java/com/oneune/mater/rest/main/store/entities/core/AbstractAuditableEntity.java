package com.oneune.mater.rest.main.store.entities.core;

import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@MappedSuperclass
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@ToString
public abstract class AbstractAuditableEntity extends AbstractEntity {
    @Builder.Default
    Instant createdAt = Instant.now();
    @Setter
    Instant updatedAt;
}
