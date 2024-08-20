package com.oneune.mater.rest;

import com.oneune.mater.rest.configs.properties.TelegramBotProperties;
import com.oneune.mater.rest.enums.Constants;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;

import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public final class MaterTelegramBot extends TelegramLongPollingBot {

    TelegramBotProperties telegramBotProperties;

    @Override
    public String getBotUsername() {
        return telegramBotProperties.getName();
    }

    @Override
    public String getBotToken() {
        return telegramBotProperties.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        init(update, this);
    }

    @SneakyThrows
    public Message init(Update update, DefaultAbsSender bot) {

        List<List<InlineKeyboardButton>> startOptionButtons = List.of(
                List.of(InlineKeyboardButton.builder()
                        .text("Start!")
                        .webApp(WebAppInfo.builder().url(Constants.URL.getField()).build())
                        .build())
        );

        InlineKeyboardMarkup startOptionsKeyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboard(startOptionButtons)
                .build();

        Message message = update.getMessage();
        SendMessage startMenuMessage = SendMessage.builder()
                .chatId(message.getChatId().toString())
                .text("""
                      Привет, %s! Я - твой помощник в выборе машин.
                      """
                        .formatted(message.getFrom().getFirstName()))
                .replyMarkup(startOptionsKeyboardMarkup)
                .build();

//        return TelegramBotUtils.uncheckedExecute(startMenuMessage, bot);
        bot.execute(startMenuMessage);
        return null;
    }
}
