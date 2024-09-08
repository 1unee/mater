package com.oneune.mater.rest.main.readers;

import com.google.gson.reflect.TypeToken;
import com.oneune.mater.rest.main.contracts.BaseQueryable;
import com.oneune.mater.rest.main.contracts.Readable;
import com.oneune.mater.rest.main.repositories.PersonalRepository;
import com.oneune.mater.rest.main.store.pagination.PageQuery;
import com.oneune.mater.rest.main.store.pagination.PageResponse;
import com.oneune.mater.rest.main.store.dtos.PersonalDto;
import com.oneune.mater.rest.main.store.entities.PersonalEntity;
import com.oneune.mater.rest.main.store.entities.QPersonalEntity;
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
public class PersonalReader implements Readable<PersonalDto, PersonalEntity>, BaseQueryable<PersonalEntity> {

    public final static Type PERSONAL_DTO_LIST = TypeToken.getParameterized(List.class, PersonalDto.class).getType();
    public final static QPersonalEntity qPersonal = new QPersonalEntity("personal");

    ModelMapper modelMapper;
    JPAQueryFactory queryFactory;
    PersonalRepository personalRepository;

    @Override
    public JPAQuery<PersonalEntity> writeBaseQuery(Predicate... predicates) {
        return queryFactory.selectFrom(qPersonal).where(predicates);
    }

    public PersonalEntity getEntityById(Long personalId) {
        return writeBaseQuery(qPersonal.id.eq(personalId)).fetchOne();
    }

    @Override
    public PersonalDto getById(Long personalId) {
        PersonalEntity personalEntity = getEntityById(personalId);
        return modelMapper.map(personalEntity, PersonalDto.class);
    }

    @Override
    public PageResponse<PersonalDto> search(PageQuery pageQuery) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
