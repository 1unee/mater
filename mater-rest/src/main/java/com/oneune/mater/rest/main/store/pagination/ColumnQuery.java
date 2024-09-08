package com.oneune.mater.rest.main.store.pagination;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.query.SortDirection;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ColumnQuery {
    String name;
    SortDirection sortDirection;
    FilterType filterType;
    Object filterValue;
}
