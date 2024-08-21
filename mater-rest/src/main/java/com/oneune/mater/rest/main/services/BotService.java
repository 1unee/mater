package com.oneune.mater.rest.main.services;

import com.oneune.mater.rest.bot.contracts.Command;
import com.oneune.mater.rest.bot.utils.TelegramBotUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class BotService implements Command {

    ButtonService buttonService;

    @Override
    public void execute(DefaultAbsSender bot, Update update) {
//        TelegramBotUtils.informAboutDeveloping(bot, update);
        start(bot, update);
    }

    public void start(DefaultAbsSender bot, Update update) {
        SendMessage startMessage = buttonService.buildStartMessage(update);
        TelegramBotUtils.uncheckedExecute(bot, startMessage);
    }
}
