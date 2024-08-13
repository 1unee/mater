package com.oneune.mater.telegram.configs;

import com.oneune.mater.telegram.configs.properties.TelegramBotProperties;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TelegramBotProperties.class)
@Log4j2
public class TelegramBotConfiguration {
}
