package com.oneune.mater.rest.main.mappers.oneune;

import com.oneune.mater.rest.main.store.entities.core.AbstractEntity;
import com.querydsl.core.JoinExpression;
import com.querydsl.core.QueryMetadata;
import com.querydsl.jpa.JPAQueryMixin;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;

import java.util.List;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class QueryDslModelMapperFactory {

    ModelMapper configuredModelMapper;

    private static List<JoinExpression> getFetchedJoinExpressions(QueryMetadata query) {
        return query.getJoins()
                .stream()
                .filter(join -> join.getFlags().contains(JPAQueryMixin.FETCH))
                .toList();
    }

    private static ModelMapper getCopiedConfiguredModelMapper(ModelMapper configuredModelMapper,
                                                              List<JoinExpression> fetchedJoins) {
        ModelMapper stuffModelMapper = new ModelMapper();
        stuffModelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
        ModelMapper copiedConfiguredModelMapper = stuffModelMapper.map(configuredModelMapper, ModelMapper.class);
        copiedConfiguredModelMapper.getConfiguration()
                .setPropertyCondition(new QueryDslFetchedJoinCondition(fetchedJoins));
        return copiedConfiguredModelMapper;
    }

    public static <E extends AbstractEntity> ModelMapper from(JPAQuery<E> query,
                                                              ModelMapper configuredModelMapper) {
        List<JoinExpression> fetchedJoins = getFetchedJoinExpressions(query.getMetadata());
        return getCopiedConfiguredModelMapper(configuredModelMapper, fetchedJoins);
    }

    public <E extends AbstractEntity> ModelMapper from(JPAQuery<E> query) {
        List<JoinExpression> fetchedJoins = getFetchedJoinExpressions(query.getMetadata());
        return getCopiedConfiguredModelMapper(configuredModelMapper, fetchedJoins);
    }
}
