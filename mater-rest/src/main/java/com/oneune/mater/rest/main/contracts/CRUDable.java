package com.oneune.mater.rest.main.contracts;

import com.oneune.mater.rest.main.store.dtos.AbstractDto;
import com.oneune.mater.rest.main.store.entities.core.AbstractEntity;

/**
 * Common CRUD contract
 * @param <D> dto
 */
public interface CRUDable<D extends AbstractDto, E extends AbstractEntity> extends Readable<D, E> {
    D post(D dto);
    D put(Long dtoId, D dto);
    D deleteById(Long dtoId);
}
