package com.oneune.mater.telegram.configs.properties;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@ConfigurationProperties(prefix = "telegram.bot")
@PropertySource("classpath:application.yml")
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
