package com.oneune.mater.rest.main.readers;

import com.google.gson.reflect.TypeToken;
import com.oneune.mater.rest.main.contracts.Readable;
import com.oneune.mater.rest.main.repositories.UserRepository;
import com.oneune.mater.rest.main.store.dtos.UserDto;
import com.oneune.mater.rest.main.store.entities.UserEntity;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Type;
import java.util.List;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserReader implements Readable<UserDto> {

    public final static Type USER_LIST_TYPE = TypeToken.getParameterized(List.class, UserDto.class).getType();

    UserRepository userRepository;
    ModelMapper modelMapper;
    JPAQueryFactory queryFactory;

    @Override
    public JPAQuery<UserDto> writeBaseQuery(Predicate... predicates) {
        return null;
    }

    @Override
    public UserDto getById(Long userId) {
        return null;
    }

    public UserEntity getEntityById(Long userId) {
        return null;
    }

    @Override
    public List<UserDto> search(int page, int size) {
        Page<UserEntity> paginatedUserEntities = userRepository.findAll(PageRequest.of(page, size));
        return modelMapper.map(paginatedUserEntities.getContent(), USER_LIST_TYPE);
    }
}
