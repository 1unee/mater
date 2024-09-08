package com.oneune.mater.rest.bot.configs;

import com.oneune.mater.rest.bot.configs.properties.TelegramBotProperties;
import com.oneune.mater.rest.common.aop.annotations.ConfigurationBeansInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TelegramBotProperties.class)
@ConfigurationBeansInfo
@Log4j2
public class TelegramBotConfiguration {


}
