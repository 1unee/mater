package com.oneune.mater.rest.store.dtos;

import com.oneune.mater.rest.contracts.Identifiable;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public abstract class AbstractDto implements Identifiable {
    @Setter(AccessLevel.PRIVATE)
    Long id;
}
