package com.oneune.mater.rest.bot;

import com.oneune.mater.rest.bot.contracts.AbstractLongPollingTelegramBot;
import com.oneune.mater.rest.bot.dispatchers.CommandDispatcher;
import com.oneune.mater.rest.bot.utils.DispatcherUtils;
import com.oneune.mater.rest.bot.utils.TelegramBotUtils;
import com.oneune.mater.rest.configs.properties.TelegramBotProperties;
import com.oneune.mater.rest.store.enums.UpdateType;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
public final class MaterTelegramBot extends AbstractLongPollingTelegramBot {

    TelegramBotProperties telegramBotProperties;
    CommandDispatcher commandDispatcher;

    public MaterTelegramBot(TelegramBotProperties telegramBotProperties,
                            CommandDispatcher commandDispatcher) {
        super(telegramBotProperties);
        this.telegramBotProperties = telegramBotProperties;
        this.commandDispatcher = commandDispatcher;
    }

    @Override
    public void distribute(DefaultAbsSender bot, Update update) {
        UpdateType updateType = DispatcherUtils.classifyUpdate(update);
        switch (updateType) {
            case COMMAND -> commandDispatcher.distribute(bot, update);
            case UNKNOWN -> TelegramBotUtils.handleUnknownUpdateType(update, bot);
        }
    }
}
