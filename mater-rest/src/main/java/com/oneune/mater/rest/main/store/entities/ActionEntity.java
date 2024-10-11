package com.oneune.mater.rest.main.store.entities;

import com.oneune.mater.rest.main.store.entities.core.AbstractEntity;
import com.oneune.mater.rest.main.store.enums.ActionTypeEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Entity
@Table(name = "action")
@SequenceGenerator(sequenceName = "action_id_seq", name = "id_seq", allocationSize = 1)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class ActionEntity extends AbstractEntity {
    Long userId;
    String body;
    @Enumerated(EnumType.STRING)
    ActionTypeEnum type;
    @Builder.Default
    Instant timestamp = Instant.now();
}
