package com.oneune.mater.rest.main.readers;

import com.google.gson.reflect.TypeToken;
import com.oneune.mater.rest.main.contracts.BaseQueryable;
import com.oneune.mater.rest.main.contracts.Readable;
import com.oneune.mater.rest.main.mappers.custom_query_dsl.ExtJPAQueryFactory;
import com.oneune.mater.rest.main.store.dtos.UserDto;
import com.oneune.mater.rest.main.store.entities.QUserEntity;
import com.oneune.mater.rest.main.store.entities.QUserRoleLinkEntity;
import com.oneune.mater.rest.main.store.entities.UserEntity;
import com.oneune.mater.rest.main.store.pagination.PageQuery;
import com.oneune.mater.rest.main.store.pagination.PageResponse;
import com.oneune.mater.rest.main.store.pagination.PaginationService;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import static com.oneune.mater.rest.main.readers.CarReader.qCar;
import static com.oneune.mater.rest.main.readers.PersonalReader.qPersonal;
import static com.oneune.mater.rest.main.readers.PhotoReader.qPhoto;
import static com.oneune.mater.rest.main.readers.RoleReader.qRole;
import static com.oneune.mater.rest.main.readers.SellerReader.qSeller;
import static com.oneune.mater.rest.main.readers.VideoReader.qVideo;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserReader implements Readable<UserDto, UserEntity>, BaseQueryable<UserEntity> {

    public final static Type USER_LIST_TYPE = TypeToken.getParameterized(List.class, UserDto.class).getType();
    public final static QUserEntity qUser = new QUserEntity("user");
    public final static QUserRoleLinkEntity qUserRoleLink = new QUserRoleLinkEntity("user_role_link");

    ModelMapper modelMapper;
    JPAQueryFactory queryFactory;
    EntityManager entityManager;
    ExtJPAQueryFactory extQueryFactory;
    PaginationService<UserDto, UserEntity> paginationService;

    @Override
    public JPAQuery<UserEntity> writeBaseQuery(Predicate... predicates) {
        return queryFactory.selectFrom(qUser).where(predicates);
    }

    public JPAQuery<UserEntity> writeLightQuery(Predicate... predicates) {
        return writeBaseQuery(predicates)
                .join(qUserRoleLink).on(qUserRoleLink.user.id.eq(qUser.id)).fetchJoin()
                .join(qRole).on(qRole.id.eq(qUserRoleLink.role.id)).fetchJoin()
                .join(qPersonal).on(qUser.personal.id.eq(qPersonal.id)).fetchJoin()
                .join(qSeller).on(qUser.seller.id.eq(qSeller.id)).fetchJoin();
    }

    public JPAQuery<UserEntity> writeHeavyQuery(Predicate... predicates) {
        return writeLightQuery(predicates)
                .join(qCar).on(qCar.seller.id.eq(qSeller.id)).fetchJoin()
                .join(qPhoto).on(qPhoto.car.id.eq(qCar.id)).fetchJoin()
                .join(qVideo).on(qVideo.car.id.eq(qCar.id)).fetchJoin();
    }

    public UserEntity getEntityById(Long userId) {
        return writeLightQuery(qUser.id.eq(userId)).fetchOne();
    }

    @Override
    public UserDto getById(Long userId) {
        UserEntity userEntity = getEntityById(userId);
        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public PageResponse<UserDto> search(PageQuery pageQuery) {
        return paginationService.process(pageQuery, UserDto.class, writeLightQuery(), "user");
    }

    public Optional<UserDto> getByUsername(String username) {
//        List<UserEntity> userEntities = writeLightQuery(qUser.username.eq(username)).fetch();
//        List<UserDto> userDtos = QueryMappers.mapUsers(
//                userEntities, true, true, true,
//                false, false, false, false
//        );

        JPAQuery<UserDto> userDtoJPAQuery = extQueryFactory.fromSelectDto(qUser).selectDto(UserDto.class);
        List<UserDto> userDtos = userDtoJPAQuery
                .join(qPersonal).on(qPersonal.id.eq(qUser.personal.id))
                .where(qUser.username.eq(username))
                .fetch();
        return userDtos.isEmpty() ? Optional.empty() : Optional.ofNullable(userDtos.get(0));
    }
}
