package com.oneune.mater.rest.main.mappers.custom_query_dsl;

import com.oneune.mater.rest.main.store.dtos.AbstractDto;
import com.oneune.mater.rest.main.store.entities.AbstractEntity;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.types.CollectionExpression;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class ExtJpaQuery<E extends AbstractEntity> extends JPAQuery<E> {

    public ExtJpaQuery() {
    }

    public ExtJpaQuery(EntityManager em) {
        super(em);
    }

    public ExtJpaQuery(EntityManager em, QueryMetadata metadata) {
        super(em, metadata);
    }

    public ExtJpaQuery(EntityManager em, JPQLTemplates templates) {
        super(em, templates);
    }

    public ExtJpaQuery(EntityManager em, JPQLTemplates templates, QueryMetadata metadata) {
        super(em, templates, metadata);
    }

    public final <D extends AbstractDto> JPAQuery<D> selectDto(Class<D> dtoClass) {
        Expression<D> projection = createProjection(dtoClass);
        return select(projection).from(queryMixin.getMetadata().getProjection());
    }

    @SafeVarargs
    public final <D extends AbstractDto> JPAQuery<D> selectDto(Class<D> dtoClass,
                                                               Expression<E> ... overrideAliases) {
        Expression<D> projection = createProjection(dtoClass, overrideAliases);
        return this.select(projection);
    }

    private <D extends AbstractDto> Expression<D> createProjection(Class<D> dtoClass,
                                                                   Expression<?>... overrideAliases) {
        Expression<?> qEntityClass = Objects.requireNonNull(queryMixin.getMetadata().getProjection());

        List<Expression<?>> parsedFields = new LinkedList<>();
        extractPathFields(qEntityClass, parsedFields);
        List<Expression<?>> parsedNonCollectionFields = parsedFields.stream().filter(pf -> !(pf instanceof CollectionExpression<?, ?>)).toList();


        JPAQuery<D> select = select(Projections.fields(dtoClass, parsedNonCollectionFields.toArray(Expression[]::new)));
        return null;
    }

    private boolean isOkField(Field field) {
        return Modifier.isPublic(field.getModifiers())
                && !Modifier.isStatic(field.getModifiers())
                && Modifier.isFinal(field.getModifiers())
                && !field.getName().startsWith("_");
    }

    private boolean isQEntityClass(Expression<?> expression) {
        return Arrays.stream(expression.getClass().getDeclaredFields())
                .anyMatch(field -> field.getName().equals("_super"));
    }

    private void extractPathFields(Expression<?> pathField,
                                   final List<Expression<?>> parsedFields) {
        if (isQEntityClass(pathField)) {
            Arrays.stream(pathField.getClass().getDeclaredFields())
                    .filter(this::isOkField)
                    .map(field -> ReflectionUtils.getField(field, pathField))
                    .map(field -> (Expression<?>) field)
                    .forEach(_pathField -> extractPathFields(_pathField, parsedFields));
        } else {
            parsedFields.add(pathField);
        }
    }
}
