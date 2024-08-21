package com.oneune.mater.rest.main.readers;

import com.oneune.mater.rest.main.contracts.Readable;
import com.oneune.mater.rest.main.store.dtos.PhotoDto;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PhotoReader implements Readable<PhotoDto> {

    @Override
    public JPAQuery<PhotoDto> writeBaseQuery(Predicate... predicates) {
        return null;
    }

    @Override
    public PhotoDto getById(Long dtoId) {
        return null;
    }

    @Override
    public List<PhotoDto> search(int page, int size) {
        return null;
    }
}
