package com.oneune.mater.rest.main.configs;

import com.oneune.mater.rest.common.aop.annotations.ConfigurationBeansInfo;
import com.oneune.mater.rest.main.mappers.oneune.QueryDslModelMapperFactory;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

//    @Bean
//    public ObjectMapper objectMapper() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        return objectMapper;
//    }
}
