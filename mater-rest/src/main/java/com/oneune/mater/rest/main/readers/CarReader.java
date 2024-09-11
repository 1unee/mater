package com.oneune.mater.rest.main.readers;

import com.google.gson.reflect.TypeToken;
import com.oneune.mater.rest.main.contracts.BaseQueryable;
import com.oneune.mater.rest.main.contracts.Readable;
import com.oneune.mater.rest.main.store.dtos.CarDto;
import com.oneune.mater.rest.main.store.entities.CarEntity;
import com.oneune.mater.rest.main.store.entities.QCarEntity;
import com.oneune.mater.rest.main.store.pagination.PageQuery;
import com.oneune.mater.rest.main.store.pagination.PageResponse;
import com.oneune.mater.rest.main.store.pagination.PaginationService;
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

import static com.oneune.mater.rest.main.readers.ContactReader.qContact;
import static com.oneune.mater.rest.main.readers.PhotoReader.qPhoto;
import static com.oneune.mater.rest.main.readers.SellerReader.qSeller;
import static com.oneune.mater.rest.main.readers.VideoReader.qVideo;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CarReader implements Readable<CarDto, CarEntity>, BaseQueryable<CarEntity> {

    public final static Type CAR_DTO_LIST = TypeToken.getParameterized(List.class, CarDto.class).getType();
    public final static QCarEntity qCar = new QCarEntity("car");

    JPAQueryFactory queryFactory;
    PaginationService<CarDto, CarEntity> paginationService;

    ModelMapper modelMapper;

    @Override
    public JPAQuery<CarEntity> writeBaseQuery(Predicate... predicates) {
        return queryFactory.selectFrom(qCar).where(predicates);
    }

    public JPAQuery<CarEntity> writeLightQuery(Predicate... predicates) {
        return writeBaseQuery(predicates)
                .join(qSeller).on(qCar.seller.id.eq(qSeller.id)).fetchJoin()
                .join(qContact).on(qContact.seller.id.eq(qContact.id)).fetchJoin();
    }

    public JPAQuery<CarEntity> writeHeavyQuery(Predicate... predicates) {
        return writeLightQuery(predicates)
                .leftJoin(qPhoto).on(qPhoto.car.id.eq(qCar.id)).fetchJoin()
                .leftJoin(qVideo).on(qVideo.car.id.eq(qCar.id)).fetchJoin();
    }

    public CarEntity getEntityById(Long carId) {
        return writeHeavyQuery(qCar.id.eq(carId)).fetchOne();
    }

    public CarEntity getExcludeFiles(Long carId) {
        return writeLightQuery(qCar.id.eq(carId)).fetchOne();
    }

    @Override
    public CarDto getById(Long carId) {
        CarEntity carEntity = writeHeavyQuery(qCar.id.eq(carId)).fetchOne();
        return modelMapper.map(carEntity, CarDto.class);
    }

    @Override
    public PageResponse<CarDto> search(PageQuery pageQuery) {
        return paginationService.process(pageQuery, CarDto.class, writeLightQuery(), "car");
    }
}
