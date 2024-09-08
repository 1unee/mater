package com.oneune.mater.rest.main.store.dtos;

import com.oneune.mater.rest.main.store.entities.core.FileEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

/**
 * @see FileEntity
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class FileDto extends AbstractDto {
    String name;
    String type;
    String base64;
}
