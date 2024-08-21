package com.oneune.mater.rest.main.contracts;

import com.oneune.mater.rest.main.store.dtos.AbstractDto;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;

public interface Readable<D extends AbstractDto> extends Searchable<D> {

    /**
     * Includes all possible joins.
     */
    JPAQuery<D> writeBaseQuery(Predicate ... predicates);
    D getById(Long dtoId);
}
