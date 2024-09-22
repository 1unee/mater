package com.oneune.mater.rest.main.mappers.oneune;

import com.oneune.mater.rest.main.store.entities.core.AbstractEntity;
import com.querydsl.core.JoinExpression;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EntityPathBase;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.atteo.evo.inflector.English;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.proxy.HibernateProxy;
import org.modelmapper.Condition;
import org.modelmapper.spi.MappingContext;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class QueryDslFetchedJoinCondition implements Condition<Object, Object> {

    private static final String DESTINATION_PATH = "destinationPath";
    private static final String REG_EXP = "\\.";

    List<JoinExpression> fetchedJoins;

    @Override
    public boolean applies(MappingContext<Object, Object> context) {
        Object source = context.getSource();
        if (source instanceof AbstractEntity) {
            return getStringfiedTargetJoins(false).contains(getJoiningFieldName(context));
        } else if (source instanceof HibernateProxy) {
            // fix setting not joined entities (one-to-one)
            return false;
        } else if (source instanceof PersistentCollection) {
            return getStringfiedTargetJoins(true).contains(getJoiningFieldName(context));
        } else {
            return true;
        }
    }

    private List<String> getStringfiedTargetJoins(boolean plural) {
        return fetchedJoins.stream()
                .map(JoinExpression::getTarget)
                .map(target -> (EntityPathBase<? extends AbstractEntity>) target)
                .map(EntityPathBase::getMetadata)
                .map(PathMetadata::getElement)
                .map(element -> (String) element)
                .map(joiningEntityName -> plural ? English.plural(joiningEntityName) : joiningEntityName)
                .toList();
    }

    private String getJoiningFieldName(MappingContext<Object, Object> context) {
        Field joiningFieldReference = ReflectionUtils.findField(context.getClass(), DESTINATION_PATH);
        Optional.ofNullable(joiningFieldReference).orElseThrow(() -> new IllegalStateException("Destination path not found!"));
        joiningFieldReference.setAccessible(true);

        if (joiningFieldReference.isAnnotationPresent(QueryDslModelMapperProperty.class)) {
            QueryDslModelMapperProperty annotation = joiningFieldReference.getAnnotation(QueryDslModelMapperProperty.class);
            // todo: implement
        }

        String destinationPathValue = (String) ReflectionUtils.getField(joiningFieldReference, context);
        Optional.ofNullable(destinationPathValue).orElseThrow(() -> new IllegalStateException("Destination path value is null!"));
        String[] pathItems = destinationPathValue.split(REG_EXP);
        return pathItems[pathItems.length - 1];
    }
}
