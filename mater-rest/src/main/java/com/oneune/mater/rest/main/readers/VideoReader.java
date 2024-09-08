package com.oneune.mater.rest.main.readers;

import com.google.gson.reflect.TypeToken;
import com.oneune.mater.rest.main.contracts.BaseQueryable;
import com.oneune.mater.rest.main.contracts.Readable;
import com.oneune.mater.rest.main.repositories.VideoRepository;
import com.oneune.mater.rest.main.store.pagination.PageQuery;
import com.oneune.mater.rest.main.store.pagination.PageResponse;
import com.oneune.mater.rest.main.store.dtos.VideoDto;
import com.oneune.mater.rest.main.store.entities.QVideoEntity;
import com.oneune.mater.rest.main.store.entities.VideoEntity;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Type;
import java.util.List;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class VideoReader implements Readable<VideoDto, VideoEntity>, BaseQueryable<VideoEntity> {

    public final static Type VIDEO_DTO_LIST = TypeToken.getParameterized(List.class, VideoDto.class).getType();
    public final static QVideoEntity qVideo = new QVideoEntity("video");

    ModelMapper modelMapper;
    JPAQueryFactory queryFactory;
    VideoRepository videoRepository;

    @Override
    public JPAQuery<VideoEntity> writeBaseQuery(Predicate... predicates) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public VideoEntity getEntityById(Long videoId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public VideoDto getById(Long videoId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PageResponse<VideoDto> search(PageQuery pageQuery) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
