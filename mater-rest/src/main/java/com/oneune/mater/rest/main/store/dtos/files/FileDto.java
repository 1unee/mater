package com.oneune.mater.rest.main.store.dtos.files;

import com.oneune.mater.rest.main.store.dtos.core.AbstractDto;
import com.oneune.mater.rest.main.store.entities.core.AbstractFileEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

/**
 * @see AbstractFileEntity
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class FileDto extends AbstractDto {
    String name;
    String type;
    Long size;
    String url;
}
