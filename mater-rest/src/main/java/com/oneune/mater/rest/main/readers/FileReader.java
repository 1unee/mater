package com.oneune.mater.rest.main.readers;

import com.google.gson.reflect.TypeToken;
import com.oneune.mater.rest.main.contracts.BaseQueryable;
import com.oneune.mater.rest.main.contracts.Readable;
import com.oneune.mater.rest.main.repositories.CarFileRepository;
import com.oneune.mater.rest.main.store.dtos.FileDto;
import com.oneune.mater.rest.main.store.entities.CarFileEntity;
import com.oneune.mater.rest.main.store.entities.QCarFileEntity;
import com.oneune.mater.rest.main.store.pagination.PageQuery;
import com.oneune.mater.rest.main.store.pagination.PageResponse;
import com.oneune.mater.rest.main.store.dtos.PhotoDto;
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
public class FileReader implements Readable<FileDto, CarFileEntity>, BaseQueryable<CarFileEntity> {

    public final static Type FILE_DTO_LIST = TypeToken.getParameterized(List.class, FileDto.class).getType();
    public final static QCarFileEntity qCarFile = new QCarFileEntity("file");

    ModelMapper modelMapper;
    JPAQueryFactory queryFactory;
    CarFileRepository carFileRepository;

    @Override
    public JPAQuery<CarFileEntity> writeBaseQuery(Predicate... predicates) {
        return queryFactory.selectFrom(qCarFile).where(predicates).orderBy(qCarFile.id.asc());
    }

    public CarFileEntity getEntityById(Long fileId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PhotoDto getById(Long fileId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PageResponse<FileDto> search(PageQuery pageQuery) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
