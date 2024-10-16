package com.oneune.mater.rest.main.configs.properties;

import com.oneune.mater.rest.common.aop.annotations.ConfigurationPropertiesInfo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

@ConfigurationProperties(prefix = "cron")
@PropertySource("classpath:application.yml")
@ConfigurationPropertiesInfo
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class CronProperties {

    Map<String, CronItem> config;

    public static class Key {
        public static final String CLEAN_ORPHAN_SELECTEL_OBJECTS = "clean-orphan-selectel-objects";
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Data
    public static class CronItem {
        String name;
        String expression;
    }
}
