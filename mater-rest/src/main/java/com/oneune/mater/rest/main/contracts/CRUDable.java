package com.oneune.mater.rest.main.contracts;

import com.oneune.mater.rest.main.store.dtos.AbstractDto;

/**
 * Common CRUD contract
 * @param <D> dto
 */
public interface CRUDable<D extends AbstractDto> extends Readable<D> {
    D post(D dto);
    D put(Long dtoId, D dto);
    D deleteById(Long dtoId);
}
