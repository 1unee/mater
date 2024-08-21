package com.oneune.mater.rest.main.contracts;

import com.oneune.mater.rest.main.store.dtos.AbstractDto;

import java.util.List;

@FunctionalInterface
public interface Searchable<D extends AbstractDto> {
    List<D> search(int page, int size);
}
