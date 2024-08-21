package com.oneune.mater.rest.main.readers;

import com.oneune.mater.rest.main.contracts.Readable;
import com.oneune.mater.rest.main.store.dtos.VideoDto;
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
public class VideoReader implements Readable<VideoDto> {

    @Override
    public JPAQuery<VideoDto> writeBaseQuery(Predicate... predicates) {
        return null;
    }

    @Override
    public VideoDto getById(Long dtoId) {
        return null;
    }

    @Override
    public List<VideoDto> search(int page, int size) {
        return null;
    }
}
