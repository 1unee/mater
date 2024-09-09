package com.oneune.mater.rest.main.mappers.custom_query_dsl;

import com.oneune.mater.rest.main.store.entities.AbstractEntity;
import com.querydsl.core.types.EntityPath;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExtJPAQueryFactory extends JPAQueryFactory {

    final EntityManager extEntityManager;

    public ExtJPAQueryFactory(EntityManager entityManager) {
        super(entityManager);
        this.extEntityManager = entityManager;
    }

    public ExtJPAQueryFactory(JPQLTemplates templates, EntityManager entityManager) {
        super(templates, entityManager);
        this.extEntityManager = entityManager;
    }

    public <T extends AbstractEntity> ExtJpaQuery<T> fromSelectDto(EntityPath<T> qClass) {
        ExtJpaQuery<T> extJpaQuery = new ExtJpaQuery<>(extEntityManager);
        extJpaQuery.select(qClass).from(qClass);
        return extJpaQuery;
    }
}
