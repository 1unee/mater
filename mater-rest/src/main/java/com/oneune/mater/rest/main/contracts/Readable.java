package com.oneune.mater.rest.main.contracts;

import com.oneune.mater.rest.main.store.dtos.AbstractDto;
import com.oneune.mater.rest.main.store.entities.AbstractEntity;

public interface Readable<D extends AbstractDto,
                          E extends AbstractEntity> extends Searchable<D> {
    D getById(Long dtoId);
}
