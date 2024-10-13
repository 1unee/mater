package com.oneune.mater.rest.main.store.dtos;

import com.oneune.mater.rest.main.store.dtos.files.FileDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@SuperBuilder
@Data
public class VideoDto extends FileDto {
}
