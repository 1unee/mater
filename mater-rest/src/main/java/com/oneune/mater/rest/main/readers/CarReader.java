package com.oneune.mater.rest.main.readers;

import com.google.gson.reflect.TypeToken;
import com.oneune.mater.rest.main.contracts.Readable;
import com.oneune.mater.rest.main.repositories.CarRepository;
import com.oneune.mater.rest.main.store.dtos.CarDto;
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
public class CarReader implements Readable<CarDto> {

    public final static Type CAR_DTO_LIST = TypeToken.getParameterized(List.class, CarDto.class).getType();

    ModelMapper modelMapper;
    JPAQueryFactory queryFactory;
    CarRepository carRepository;

    @Override
    public JPAQuery<CarDto> writeBaseQuery(Predicate... predicates) {
        return null;
    }

    @Override
    public CarDto getById(Long dtoId) {
        return null;
    }

    @Override
    public List<CarDto> search(int page, int size) {
        return null;
    }
}
