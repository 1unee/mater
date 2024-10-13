package com.oneune.mater.rest.main.configs;

import com.oneune.mater.rest.common.aop.annotations.ConfigurationBeansInfo;
import com.oneune.mater.rest.main.configs.properties.CronProperties;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableConfigurationProperties(CronProperties.class)
@EnableScheduling
@ConfigurationBeansInfo
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SchedulerConfig {
}
