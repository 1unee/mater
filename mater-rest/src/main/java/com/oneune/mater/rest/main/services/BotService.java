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
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class BotService implements Command {

    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    ButtonBuilderService buttonBuilderService;

    @Override
    public void execute(DefaultAbsSender bot, Update update) {
        SendMessage inlineButtonKeyboard = buttonBuilderService.buildStartMessageWithOnlyText(update);
        TelegramBotUtils.uncheckedExecute(bot, inlineButtonKeyboard);
        deleteStartMessageAsync(bot, update.getMessage());
    }

    private void deleteStartMessageAsync(DefaultAbsSender bot, Message message) {
        Runnable task = () -> TelegramBotUtils.deleteMessage(bot, message.getChatId(), message.getMessageId());
        scheduler.schedule(task, 30, TimeUnit.SECONDS);
    }
}
