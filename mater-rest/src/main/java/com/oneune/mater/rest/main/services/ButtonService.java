package com.oneune.mater.rest.main.services;

import com.oneune.mater.rest.bot.utils.TelegramBotUtils;
import com.oneune.mater.rest.main.store.enums.Constants;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class ButtonService {

    private final static String DEFAULT_KEYBOARD_BUTTONS_MESSAGE = "Выбери нужную опцию ниже";

    public SendMessage buildStartMessage(Update update) {

        List<List<InlineKeyboardButton>> startOptionButtons = List.of(
                List.of(InlineKeyboardButton.builder()
                        .text("Start!")
//                        .url("https://oneune.duckdns.org:8081/test")
                        .webApp(WebAppInfo.builder().url(Constants.WEB_URL.getValue()).build())
                        .build())
        );

        InlineKeyboardMarkup startOptionsKeyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboard(startOptionButtons)
                .build();

        Message message = update.getMessage();
        return SendMessage.builder()
                .chatId(message.getChatId().toString())
                .text("""
                      Привет, %s! Я - твой помощник в выборе машин.
                      """
                        .formatted(message.getFrom().getFirstName()))
                .replyMarkup(startOptionsKeyboardMarkup)
                .build();
    }

    public SendMessage buildStartKeyboardButtons(Update update) {

        List<KeyboardButton> row1 = List.of(
                KeyboardButton.builder()
                        .text("Начать с короткого знакомства")
                        .webApp(WebAppInfo.builder().url(Constants.WEB_URL.getValue() + "/start").build())
                        .build()
        );
        List<KeyboardRow> rows = List.of(new KeyboardRow(row1));

        ReplyKeyboardMarkup keyboardMarkup = ReplyKeyboardMarkup.builder()
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .keyboard(rows)
                .build();

        return SendMessage.builder()
                .chatId(TelegramBotUtils.extractChatId(update).toString())
                .text(DEFAULT_KEYBOARD_BUTTONS_MESSAGE)
                .replyMarkup(keyboardMarkup)
                .build();
    }

    public SendMessage buildCarsKeyboardButtons(Update update) {

        List<KeyboardButton> row1 = List.of(
                KeyboardButton.builder()
                        .text("Список машин")
                        .webApp(WebAppInfo.builder().url(Constants.WEB_URL.getValue() + "/cars").build())
                        .build()
        );
        List<KeyboardButton> row2 = List.of(
                KeyboardButton.builder()
                        .text("История просмотра")
                        .build()
        );
        List<KeyboardRow> rows = List.of(new KeyboardRow(row1), new KeyboardRow(row2));

        ReplyKeyboardMarkup keyboardMarkup = ReplyKeyboardMarkup.builder()
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .keyboard(rows)
                .build();

        return SendMessage.builder()
                .chatId(TelegramBotUtils.extractChatId(update).toString())
                .text(DEFAULT_KEYBOARD_BUTTONS_MESSAGE)
                .replyMarkup(keyboardMarkup)
                .build();
    }
}
