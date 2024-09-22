package com.oneune.mater.rest.main.store.pagination;

import com.google.common.base.Function;
import com.google.gson.reflect.TypeToken;
import com.oneune.mater.rest.main.configs.properties.PaginationProperties;
import com.oneune.mater.rest.main.mappers.oneune.QueryDslModelMapperFactory;
import com.oneune.mater.rest.main.store.dtos.AbstractDto;
import com.oneune.mater.rest.main.store.entities.core.AbstractEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.hibernate.query.SortDirection.ASCENDING;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class PaginationService<D extends AbstractDto, E extends AbstractEntity> {

    private final static String ID = "id";

    PaginationProperties paginationProperties;
    QueryDslModelMapperFactory queryDslModelMapperFactory;

    public  PageResponse<D> process(PageQuery pageQuery,
                                    Class<D> dtoClass,
                                    JPAQuery<E> query) {
        return process(pageQuery, dtoClass, query);
    }

    public  PageResponse<D> process(PageQuery pageQuery,
                                    Class<D> dtoClass,
                                    JPAQuery<E> query,
                                    String entityTableName) {
        PathBuilder<E> qEntity = new PathBuilder<>(query.getType(), entityTableName);
        BooleanBuilder predicate = new BooleanBuilder();
        pageQuery.getColumns().stream()
                .filter(column -> Objects.nonNull(column.getFilterValue()))
                .forEach(column -> filter(predicate, qEntity, column));
        pageQuery.getColumns()
                .forEach(column -> sort(query, qEntity, column));
        query.where(predicate);

        return getPageResponse(pageQuery, dtoClass, query, entityTableName);
    }

    private PageResponse<D> getPageResponse(PageQuery pageQuery,
                                            Class<D> dtoClass,
                                            JPAQuery<E> query,
                                            String entityTableName) {

        PathBuilder<E> qEntity = new PathBuilder<>(query.getType(), entityTableName);
        NumberPath<Long> idPath = qEntity.getNumber(ID, Long.class);

        JPAQuery<E> copiedQueryWithoutOrdering = query.clone();
        copiedQueryWithoutOrdering.getMetadata().clearOrderBy();
        Long totalElements = copiedQueryWithoutOrdering
                .select(idPath.countDistinct())
                .fetchOne();

        Integer pageSize = Optional.ofNullable(pageQuery.getSize()).orElse(paginationProperties.getPageSize());

        List<E> content = query
                .distinct()
                .orderBy(idPath.asc())
                .offset((long) pageQuery.getPage() * pageSize)
                .limit(pageSize)
                .fetch();

        int totalPages = (int) Math.ceil((double) Optional.ofNullable(totalElements).orElse(0L) / pageSize);

        return PageResponse.<D>builder()
                .totalElements(totalElements)
                .totalPages(totalPages)
                // modelMapper маппит все поля (разработать универсальный метод маппинга с использованием Query DSL)
                .content(queryDslModelMapperFactory.from(query).map(
                        content, TypeToken.getParameterized(List.class, dtoClass).getType()
                ))
                .build();
    }

    private void sort(JPAQuery<E> query,
                      EntityPathBase<E> qEntity,
                      ColumnQuery column) {
        // todo: expression depends column value type
        ComparableExpressionBase<String> field = Expressions.stringPath(qEntity, column.getName());
        boolean isMissedSortDirection = Objects.isNull(column.getSortDirection());
        if (!isMissedSortDirection) {
            OrderSpecifier<?> specifier = Objects.equals(column.getSortDirection(), ASCENDING) ? field.asc() : field.desc();
            query.orderBy(specifier);
        }
    }

    private void filter(BooleanBuilder predicate,
                        PathBuilder<E> entityPath,
                        ColumnQuery column) {
        Object columnFilterValue = column.getFilterValue();

        if (columnFilterValue instanceof CharSequence) {
            filterCharSequence(predicate, entityPath, column, (CharSequence) columnFilterValue);
        } else if (columnFilterValue instanceof Number) {
            filterNumber(predicate, entityPath, column, (Number) columnFilterValue);
        } else if (columnFilterValue instanceof Temporal) {
            filterTemporal(predicate, entityPath, column, (Temporal) columnFilterValue);
        } else if (columnFilterValue instanceof Boolean) {
            filterBoolean(predicate, entityPath, column, (Boolean) columnFilterValue);
        } else if (columnFilterValue instanceof Date) {
            filterData(predicate, entityPath, column, (Date) columnFilterValue);
        } else {
            throw new IllegalArgumentException("Unsupported type %s".formatted(columnFilterValue.getClass()));
        }
    }

    private void filterCharSequence(BooleanBuilder predicate,
                                    PathBuilder<E> entityPath,
                                    ColumnQuery column,
                                    CharSequence columnFilterValue) {
        BooleanExpression booleanExpression;
        if (columnFilterValue instanceof String) {
            StringPath stringPath = entityPath.getString(column.getName());
            Function<String, BooleanExpression> stringFilteringMethod = getStringFilteringMethod(column.getFilterType(), stringPath);
            booleanExpression = stringFilteringMethod.apply((String) columnFilterValue);
            predicate.and(booleanExpression);
        } // other CharSequence implementations
    }

    private <S extends String,
            P extends StringPath> Function<S, BooleanExpression> getStringFilteringMethod(FilterType filterType,
                                                                                          P path) {
        return switch (filterType) {
            case EQUALS -> path::equalsIgnoreCase;
            case GREATER_THAN -> path::gt;
            case LESS_THAN -> path::lt;
            case STARTS_WITH -> path::startsWithIgnoreCase;
            case CONTAINS -> path::containsIgnoreCase;
            case ENDS_WITH -> path::endsWithIgnoreCase;
        };
    }

    private void filterNumber(BooleanBuilder predicate,
                              PathBuilder<E> entityPath,
                              ColumnQuery column,
                              Number columnFilterValue) {
        if (columnFilterValue instanceof Integer) {
            NumberPath<Integer> integerNumberPath = entityPath.getNumber(column.getName(), Integer.class);
            Function<Integer, BooleanExpression> integerFilteringMethod = getNumberFilteringMethod(
                    column.getFilterType(), integerNumberPath
            );
            predicate.and(integerFilteringMethod.apply((Integer) columnFilterValue));
        } else if (columnFilterValue instanceof Long) {
            NumberPath<Long> longNumberPath = entityPath.getNumber(column.getName(), Long.class);
            Function<Long, BooleanExpression> longFilteringMethod = getNumberFilteringMethod(
                    column.getFilterType(), longNumberPath
            );
            predicate.and(longFilteringMethod.apply((Long) columnFilterValue));
        } else if (columnFilterValue instanceof Float) {
            NumberPath<Float> floatNumberPath = entityPath.getNumber(column.getName(), Float.class);
            Function<Float, BooleanExpression> floatFilteringMethod = getNumberFilteringMethod(
                    column.getFilterType(), floatNumberPath
            );
            predicate.and(floatFilteringMethod.apply((Float) columnFilterValue));
        } else if (columnFilterValue instanceof Double) {
            NumberPath<Double> doubleNumberPath = entityPath.getNumber(column.getName(), Double.class);
            Function<Double, BooleanExpression> floatFilteringMethod = getNumberFilteringMethod(
                    column.getFilterType(), doubleNumberPath
            );
            predicate.and(floatFilteringMethod.apply((Double) columnFilterValue));
        } else {
            throw new IllegalArgumentException("Unsupported number type: " + columnFilterValue.getClass());
        } // other Number implementations
    }

    private <N extends Number & Comparable<N>,
            P extends NumberPath<N>> Function<N, BooleanExpression> getNumberFilteringMethod(FilterType filterType,
                                                                                             P path) {
        return switch (filterType) {
            case EQUALS -> path::eq;
            case GREATER_THAN -> path::gt;
            case LESS_THAN -> path::lt;
            case STARTS_WITH, ENDS_WITH, CONTAINS -> throw new IllegalArgumentException(
                    "Unsupported filter type <%s> for Number path".formatted(filterType)
            );
        };
    }

    private void filterTemporal(BooleanBuilder predicate,
                                PathBuilder<E> entityPath,
                                ColumnQuery column,
                                Temporal columnFilterValue) {
        if (columnFilterValue instanceof LocalDate) {

        } else if (columnFilterValue instanceof LocalTime) {

        } else if (columnFilterValue instanceof LocalDateTime) {

        } // other Temporal implementations
    }

    private void filterBoolean(BooleanBuilder predicate,
                               PathBuilder<E> entityPath,
                               ColumnQuery column,
                               Boolean columnFilterValue) {
        throw new IllegalStateException("Not implemented yet.");
    }

    private void filterData(BooleanBuilder predicate,
                            PathBuilder<E> entityPath,
                            ColumnQuery column,
                            Date columnFilterValue) {
        if (columnFilterValue instanceof Date) {

        } // other Date implementations
    }
}
