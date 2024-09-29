package com.oneune.mater.rest.main.configs;

import com.oneune.mater.rest.common.aop.annotations.ConfigurationBeansInfo;
import com.oneune.mater.rest.main.mappers.oneune.QueryDslModelMapperFactory;
import com.oneune.mater.rest.main.store.dtos.RoleDto;
import com.oneune.mater.rest.main.store.dtos.UserDto;
import com.oneune.mater.rest.main.store.entities.UserEntity;
import com.oneune.mater.rest.main.store.entities.UserRoleLinkEntity;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@ConfigurationBeansInfo
public class SerializationConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(AccessLevel.PRIVATE);
        return modelMapper;
    }

    @Bean
    public QueryDslModelMapperFactory queryDslModelMapperFactory(ModelMapper modelMapper) {
        return new QueryDslModelMapperFactory(modelMapper);
    }

    @Bean
    public ModelMapper userModelMapper() {
        ModelMapper modelMapper = modelMapper();

        Converter<List<UserRoleLinkEntity>, List<RoleDto>> roleConverter = ctx -> ctx.getSource().stream()
                        .map(UserRoleLinkEntity::getRole)
                        .map(roleEntity -> modelMapper.map(roleEntity, RoleDto.class))
                        .collect(Collectors.toList());

        modelMapper.addMappings(new PropertyMap<UserEntity, UserDto>() {
            @Override
            protected void configure() {
                using(roleConverter).map(source.getUserRoleLinks()).setRoles(null);
            }
        });

        return modelMapper;
    }

//    @Bean
//    public ObjectMapper objectMapper() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        return objectMapper;
//    }
}
