package com.oneune.mater.rest.main.readers;

import com.google.gson.reflect.TypeToken;
import com.oneune.mater.rest.main.contracts.BaseQueryable;
import com.oneune.mater.rest.main.contracts.Readable;
import com.oneune.mater.rest.main.store.dtos.CarDto;
import com.oneune.mater.rest.main.store.entities.CarEntity;
import com.oneune.mater.rest.main.store.entities.QCarEntity;
import com.oneune.mater.rest.main.store.entities.QUserFavoriteCarLinkEntity;
import com.oneune.mater.rest.main.store.entities.UserFavoriteCarLinkEntity;
import com.oneune.mater.rest.main.store.enums.SaleStatusEnum;
import com.oneune.mater.rest.main.store.pagination.PageQuery;
import com.oneune.mater.rest.main.store.pagination.PageResponse;
import com.oneune.mater.rest.main.store.pagination.PaginationService;
import com.querydsl.core.Tuple;
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
import static com.oneune.mater.rest.main.readers.FileReader.qCarFile;
import static com.oneune.mater.rest.main.readers.SellerReader.qSaleLink;
import static com.oneune.mater.rest.main.readers.SellerReader.qSeller;
import static com.oneune.mater.rest.main.readers.SettingReader.*;
import static com.oneune.mater.rest.main.readers.UserReader.qUser;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CarReader implements Readable<CarDto, CarEntity>, BaseQueryable<CarEntity> {

    public final static Type CAR_DTO_LIST = TypeToken.getParameterized(List.class, CarDto.class).getType();
    public final static QCarEntity qCar = new QCarEntity("car");
    public final static QUserFavoriteCarLinkEntity qUserFavoriteCarLink = new QUserFavoriteCarLinkEntity("user_favorite_car_link");

    JPAQueryFactory queryFactory;
    PaginationService<CarDto, CarEntity> paginationService;

    ModelMapper modelMapper;

    @Override
    public JPAQuery<CarEntity> writeBaseQuery(Predicate... predicates) {
        return queryFactory.selectFrom(qCar).where(predicates).orderBy(qCar.id.asc());
    }

    public JPAQuery<CarEntity> writeLightQuery(Predicate... predicates) {
        return writeBaseQuery(predicates)
                .join(qSeller).on(qCar.seller.id.eq(qSeller.id)).fetchJoin()
                .join(qContact).on(qContact.seller.id.eq(qSeller.id)).fetchJoin()
                .leftJoin(qCarFile).on(qCarFile.car.id.eq(qCar.id)).fetchJoin();
    }

    public JPAQuery<CarEntity> writeHeavyQuery(Predicate... predicates) {
        return writeLightQuery(predicates);
    }

    public CarEntity getEntityById(Long carId) {
        return writeHeavyQuery(qCar.id.eq(carId)).fetchOne();
    }

    @Override
    public CarDto getById(Long carId) {
        CarEntity carEntity = writeHeavyQuery(qCar.id.eq(carId)).fetchOne();
        return modelMapper.map(carEntity, CarDto.class);
    }

    @Override
    public PageResponse<CarDto> search(PageQuery pageQuery) {
        JPAQuery<CarEntity> query = writeLightQuery()
                .leftJoin(qSaleLink).on(qSaleLink.car.id.eq(qCar.id)
                        .and(qSaleLink.status.eq(SaleStatusEnum.BOUGHT).not())).fetchJoin();
        return paginationService.process(pageQuery, CarDto.class, query, "car");
    }

    public List<CarDto> getAll() {
        return modelMapper.map(writeHeavyQuery().fetch(), CAR_DTO_LIST);
    }

    public List<CarDto> getFavoritesByUserId(Long userId) {
        List<CarEntity> favoriteCarEntities = queryFactory.selectFrom(qCar)
                .join(qUserFavoriteCarLink).on(qUserFavoriteCarLink.car.id.eq(qCar.id))
                .join(qUser).on(qUserFavoriteCarLink.user.id.eq(qUser.id).and(qUser.id.eq(userId)))
                .fetch();
        return modelMapper.map(favoriteCarEntities, CAR_DTO_LIST);
    }

    public UserFavoriteCarLinkEntity getFavoriteLinkByIds(Long userId, Long carId) {
        return queryFactory.selectFrom(qUserFavoriteCarLink)
                .where(qUserFavoriteCarLink.user.id.eq(userId)
                        .and(qUserFavoriteCarLink.car.id.eq(carId)))
                .fetchOne();
    }

    public List<Tuple> getFavoriteCarsForUpdateEvent(Long carId) {
        JPAQuery<Tuple> query = queryFactory.select(
                        qUserSettings.user,
                        qUserFavoriteCarLink.car,
                        qUserSettingLink.selectedOption
                ).from(qUserSettings)
                .join(qUser).on(qUser.id.eq(qUserSettings.user.id))
                .join(qUserSettingLink).on(qUserSettingLink.userSettings.id.eq(qUserSettings.id))
                .join(qSetting).on(qSetting.id.eq(qUserSettingLink.setting.id)
                        .and(qSetting.code.eq(6)))
                .join(qOption).on(qOption.id.eq(qUserSettingLink.selectedOption.id))
                .join(qUserFavoriteCarLink).on(qUserFavoriteCarLink.user.id.eq(qUserSettings.user.id)
                        .and(qUserFavoriteCarLink.car.id.eq(carId)));
        return query.fetch();
    }
}
