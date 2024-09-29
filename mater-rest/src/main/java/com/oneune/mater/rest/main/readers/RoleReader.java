package com.oneune.mater.rest.main.readers;

import com.google.gson.reflect.TypeToken;
import com.oneune.mater.rest.main.contracts.BaseQueryable;
import com.oneune.mater.rest.main.contracts.Readable;
import com.oneune.mater.rest.main.repositories.RoleRepository;
import com.oneune.mater.rest.main.store.pagination.PageQuery;
import com.oneune.mater.rest.main.store.pagination.PageResponse;
import com.oneune.mater.rest.main.store.dtos.RoleDto;
import com.oneune.mater.rest.main.store.entities.QRoleEntity;
import com.oneune.mater.rest.main.store.entities.RoleEntity;
import com.oneune.mater.rest.main.store.enums.RoleEnum;
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
public class RoleReader implements Readable<RoleDto, RoleEntity>, BaseQueryable<RoleEntity> {

    public final static Type ROLE_DTO_LIST = TypeToken.getParameterized(List.class, RoleDto.class).getType();
    public final static QRoleEntity qRole = new QRoleEntity("role");

    ModelMapper modelMapper;
    JPAQueryFactory queryFactory;
    RoleRepository roleRepository;

    @Override
    public JPAQuery<RoleEntity> writeBaseQuery(Predicate... predicates) {
        return queryFactory.selectFrom(qRole).where(predicates).orderBy(qRole.id.asc());
    }

    public RoleEntity getEntityById(Long roleId) {
        return writeBaseQuery(qRole.id.eq(roleId)).fetchOne();
    }

    public RoleEntity getEntityByEnum(RoleEnum roleConst) {
        return writeBaseQuery(qRole.name.eq(roleConst)).fetchOne();
    }

    @Override
    public RoleDto getById(Long roleId) {
        RoleEntity roleEntity = getEntityById(roleId);
        return modelMapper.map(roleEntity, RoleDto.class);
    }

    @Override
    public PageResponse<RoleDto> search(PageQuery pageQuery) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<RoleDto> getRoles() {
        return modelMapper.map(writeBaseQuery().fetch(), ROLE_DTO_LIST);
    }
}
