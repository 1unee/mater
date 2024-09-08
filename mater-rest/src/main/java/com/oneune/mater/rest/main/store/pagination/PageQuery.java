package com.oneune.mater.rest.main.store.pagination;

import jakarta.annotation.Nullable;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PageQuery {
    @NonNull Integer page;
    @Nullable Integer size;
    List<ColumnQuery> columns;
}
