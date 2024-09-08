package com.oneune.mater.rest.main.readers;

import com.google.gson.reflect.TypeToken;
import com.oneune.mater.rest.main.contracts.BaseQueryable;
import com.oneune.mater.rest.main.contracts.Readable;
import com.oneune.mater.rest.main.repositories.PhotoRepository;
import com.oneune.mater.rest.main.store.pagination.PageQuery;
import com.oneune.mater.rest.main.store.pagination.PageResponse;
import com.oneune.mater.rest.main.store.dtos.PhotoDto;
import com.oneune.mater.rest.main.store.entities.PhotoEntity;
import com.oneune.mater.rest.main.store.entities.QPhotoEntity;
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
public class PhotoReader implements Readable<PhotoDto, PhotoEntity>, BaseQueryable<PhotoEntity> {

    public final static Type PHOTO_DTO_LIST = TypeToken.getParameterized(List.class, PhotoDto.class).getType();
    public final static QPhotoEntity qPhoto = new QPhotoEntity("photo");

    ModelMapper modelMapper;
    JPAQueryFactory queryFactory;
    PhotoRepository photoRepository;

    @Override
    public JPAQuery<PhotoEntity> writeBaseQuery(Predicate... predicates) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public PhotoEntity getEntityById(Long photoId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
        public PhotoDto getById(Long photoId) {throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PageResponse<PhotoDto> search(PageQuery pageQuery) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
