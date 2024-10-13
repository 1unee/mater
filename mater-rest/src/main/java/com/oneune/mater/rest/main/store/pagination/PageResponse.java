package com.oneune.mater.rest.main.store.pagination;

import com.oneune.mater.rest.main.store.dtos.core.AbstractDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PageResponse<D extends AbstractDto> {
    Long totalElements;
    Integer totalPages;
    List<D> content;
}
