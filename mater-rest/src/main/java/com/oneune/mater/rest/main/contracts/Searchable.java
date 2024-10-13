package com.oneune.mater.rest.main.contracts;

import com.oneune.mater.rest.main.store.dtos.core.AbstractDto;
import com.oneune.mater.rest.main.store.pagination.PageQuery;
import com.oneune.mater.rest.main.store.pagination.PageResponse;

@FunctionalInterface
public interface Searchable<D extends AbstractDto> {

    PageResponse<D> search(PageQuery pageQuery);
}
