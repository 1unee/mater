package com.oneune.mater.rest.main.readers;

import com.google.gson.reflect.TypeToken;
import com.oneune.mater.rest.main.contracts.BaseQueryable;
import com.oneune.mater.rest.main.contracts.Readable;
import com.oneune.mater.rest.main.repositories.ContactRepository;
import com.oneune.mater.rest.main.store.dtos.ContactDto;
import com.oneune.mater.rest.main.store.pagination.PageQuery;
import com.oneune.mater.rest.main.store.pagination.PageResponse;
import com.oneune.mater.rest.main.store.entities.ContactEntity;
import com.oneune.mater.rest.main.store.entities.QContactEntity;
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
public class ContactReader implements Readable<ContactDto, ContactEntity>, BaseQueryable<ContactEntity> {

    public final static Type CONTACT_DTO_LIST = TypeToken.getParameterized(List.class, ContactDto.class).getType();
    public final static QContactEntity qContact = new QContactEntity("contact");

    ModelMapper modelMapper;
    JPAQueryFactory queryFactory;
    ContactRepository contactRepository;

    @Override
    public JPAQuery<ContactEntity> writeBaseQuery(Predicate... predicates) {
        return queryFactory.selectFrom(qContact).where(predicates).orderBy(qContact.id.asc());
    }

    public ContactEntity getEntityById(Long contactId) {
        return writeBaseQuery(qContact.id.eq(contactId)).fetchOne();
    }

    @Override
    public ContactDto getById(Long contactId) {
        ContactEntity contactEntity = getEntityById(contactId);
        return modelMapper.map(contactEntity, ContactDto.class);
    }

    @Override
    public PageResponse<ContactDto> search(PageQuery pageQuery) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<ContactDto> getContactsBySellerId(Long sellerId) {
        List<ContactEntity> contactEntities = writeBaseQuery(qContact.seller.id.eq(sellerId)).fetch();
        return modelMapper.map(contactEntities, CONTACT_DTO_LIST);
    }
}
