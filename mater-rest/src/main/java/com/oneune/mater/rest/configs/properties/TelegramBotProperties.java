package com.oneune.mater.rest.configs.properties;

import com.oneune.mater.rest.aop.annotations.ConfigurationPropertiesInfo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@ConfigurationProperties(prefix = "telegram.bot")
@PropertySource("classpath:application.yml")
@ConfigurationPropertiesInfo
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class TelegramBotProperties {

    /**
     * Optional property.
     * Name of telegram bot.
     */
    String name = "default-telegram-bot";

    /**
     * Necessary property.
     * Generated token telegram bot.
     */
    String token;
}
