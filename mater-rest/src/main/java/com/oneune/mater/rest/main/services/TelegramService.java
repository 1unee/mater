package com.oneune.mater.rest.main.services;

import com.oneune.mater.rest.bot.utils.TelegramBotUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class TelegramService {

    DefaultAbsSender materTelegramBot;

    public Message sendMessageAboutCarEdited(Long telegramChatId, String message) {
        SendMessage carsMenuMessage = SendMessage.builder()
                .chatId(telegramChatId.toString())
                .text(message)
                .build();

        return TelegramBotUtils.uncheckedExecute(materTelegramBot, carsMenuMessage);
    }
}
