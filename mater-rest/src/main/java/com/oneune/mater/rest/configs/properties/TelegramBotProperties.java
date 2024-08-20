package com.oneune.mater.rest.configs.properties;

import com.oneune.mater.rest.aop.annotations.ConfigurationPropertiesInfo;
import com.oneune.mater.rest.bot.utils.ValidationUtils;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;
import java.util.Optional;

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

    /**
     * Optional property.
     * Used for setting webhook.
     */
    String url;

    /**
     * Telegram Bot command properties.
     * For example:
     *   command:
     *     name-of-your-command:
     *       description: description-of-your-command
     */
    Map<String, TelegramBotCommandProperties> command = Map.of("start", new TelegramBotCommandProperties());

    @PostConstruct
    public void init() {
        ValidationUtils.validateToken(token);
        ValidationUtils.validateCommands(command);
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Data
    public static class TelegramBotCommandProperties {
        String description = "This is a start command.";
    }
}
