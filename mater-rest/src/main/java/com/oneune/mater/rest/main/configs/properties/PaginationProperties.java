package com.oneune.mater.rest.main.configs.properties;

import com.oneune.mater.rest.common.aop.annotations.ConfigurationPropertiesInfo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@ConfigurationProperties(prefix = "spring.jpa.pagination")
@PropertySource("classpath:application.yml")
@ConfigurationPropertiesInfo
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class PaginationProperties {
    /**
     * Page size.
     */
    Integer pageSize = 50;
}
