package com.oneune.mater.rest.bot;

import com.oneune.mater.rest.bot.contracts.AbstractLongPollingTelegramBot;
import com.oneune.mater.rest.bot.dispatchers.CommandDispatcher;
import com.oneune.mater.rest.bot.dispatchers.WebAppDataDispatcher;
import com.oneune.mater.rest.bot.utils.DispatcherUtils;
import com.oneune.mater.rest.bot.utils.TelegramBotUtils;
import com.oneune.mater.rest.bot.configs.properties.TelegramBotProperties;
import com.oneune.mater.rest.main.repositories.UpdateRepository;
import com.oneune.mater.rest.main.store.entities.UpdateEntity;
import com.oneune.mater.rest.main.store.enums.UpdateTypeEnum;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component("materTelegramBot")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
public class MaterTelegramBot extends AbstractLongPollingTelegramBot {

    TelegramBotProperties telegramBotProperties;
    CommandDispatcher commandDispatcher;
    UpdateRepository updateRepository;
    WebAppDataDispatcher webAppDataDispatcher;

    public MaterTelegramBot(TelegramBotProperties telegramBotProperties,
                            CommandDispatcher commandDispatcher,
                            UpdateRepository updateRepository, WebAppDataDispatcher webAppDataDispatcher) {
        super(telegramBotProperties);
        this.telegramBotProperties = telegramBotProperties;
        this.commandDispatcher = commandDispatcher;
        this.updateRepository = updateRepository;
        this.webAppDataDispatcher = webAppDataDispatcher;
    }

    @Transactional
    @Override
    public void distribute(DefaultAbsSender bot, Update update) {
        updateRepository.save(new UpdateEntity(update));
        UpdateTypeEnum updateType = DispatcherUtils.classifyUpdate(update);
        switch (updateType) {
            case COMMAND -> commandDispatcher.distribute(bot, update);
            case WEB_APP_DATA -> webAppDataDispatcher.distribute(bot, update);
            case UNKNOWN -> TelegramBotUtils.handleUnknownUpdateType(update, bot);
        }
    }
}
