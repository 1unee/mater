package com.oneune.mater.rest.main.contracts;

import com.oneune.mater.rest.main.store.entities.AbstractEntity;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;

public interface BaseQueryable<E extends AbstractEntity> {
    /**
     * Includes all possible joins.
     */
    JPAQuery<E> writeBaseQuery(Predicate... predicates);
    // JPAQuery<E> writeLightQuery(Predicate... predicates);
    // JPAQuery<E> writeHeavyQuery(Predicate... predicates);
    // E getEntityById(Long entityId);
}
