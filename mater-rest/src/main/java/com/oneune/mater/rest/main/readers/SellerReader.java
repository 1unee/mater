package com.oneune.mater.rest.main.readers;

import com.google.gson.reflect.TypeToken;
import com.oneune.mater.rest.main.contracts.BaseQueryable;
import com.oneune.mater.rest.main.contracts.Readable;
import com.oneune.mater.rest.main.repositories.SellerRepository;
import com.oneune.mater.rest.main.store.dtos.SaleLinkDto;
import com.oneune.mater.rest.main.store.entities.QSaleLinkEntity;
import com.oneune.mater.rest.main.store.entities.SaleLinkEntity;
import com.oneune.mater.rest.main.store.enums.SaleStatusEnum;
import com.oneune.mater.rest.main.store.pagination.PageQuery;
import com.oneune.mater.rest.main.store.pagination.PageResponse;
import com.oneune.mater.rest.main.store.dtos.SellerDto;
import com.oneune.mater.rest.main.store.entities.QSellerEntity;
import com.oneune.mater.rest.main.store.entities.SellerEntity;
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
import java.util.Optional;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SellerReader implements Readable<SellerDto, SellerEntity>, BaseQueryable<SellerEntity> {

    public final static Type SELLER_LIST_TYPE = TypeToken.getParameterized(List.class, SellerDto.class).getType();
    public final static Type SALE_LINK_LIST_TYPE = TypeToken.getParameterized(List.class, SaleLinkDto.class).getType();
    public final static QSellerEntity qSeller = new QSellerEntity("seller");
    public final static QSaleLinkEntity qSaleLink = new QSaleLinkEntity("sale_link");

    JPAQueryFactory queryFactory;
    ModelMapper modelMapper;
    SellerRepository sellerRepository;

    @Override
    public JPAQuery<SellerEntity> writeBaseQuery(Predicate... predicates) {
        return queryFactory.selectFrom(qSeller).where(predicates).orderBy(qSeller.id.asc());
    }

    public SellerEntity getEntityById(Long sellerId) {
        return writeBaseQuery(qSeller.id.eq(sellerId)).fetchOne();
    }

    @Override
    public SellerDto getById(Long sellerId) {
        SellerEntity sellerEntity = getEntityById(sellerId);
        return modelMapper.map(sellerEntity, SellerDto.class);
    }

    @Override
    public PageResponse<SellerDto> search(PageQuery pageQuery) {
        throw new IllegalStateException("Not implemented!");
    }

    public SellerDto getBarCarId(Long carId) {
        SellerEntity sellerEntity = writeBaseQuery(qSeller.cars.any().id.eq(carId)).fetchOne();
        return modelMapper.map(sellerEntity, SellerDto.class);
    }

    public Optional<SaleLinkEntity> getSaleLinkByParams(Long buyerId, Long carId, SaleStatusEnum status) {
        List<SaleLinkEntity> saleLinkEntities = queryFactory.selectFrom(qSaleLink)
                .where(qSaleLink.buyer.id.eq(buyerId)
                        .and(qSaleLink.car.id.eq(carId))
                        .and(qSaleLink.status.eq(status)))
                .fetch();
        return saleLinkEntities.isEmpty() ? Optional.empty() : Optional.of(saleLinkEntities.get(0));
    }

    public List<SaleLinkDto> getSalesByBuyerId(Long buyerId) {
        List<SaleLinkEntity> saleLinkEntities = queryFactory.selectFrom(qSaleLink)
                .where(qSaleLink.buyer.id.eq(buyerId))
                .fetch();
        return modelMapper.map(saleLinkEntities, SALE_LINK_LIST_TYPE);
    }

    public SaleLinkEntity getSaleLinkById(Long saleLinkId) {
        return queryFactory.selectFrom(qSaleLink)
                .where(qSaleLink.id.eq(saleLinkId))
                .fetchOne();
    }
}
