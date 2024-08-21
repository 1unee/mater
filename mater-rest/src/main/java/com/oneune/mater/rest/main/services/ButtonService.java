package com.oneune.mater.rest.main.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class ButtonService {

    public SendMessage buildStartMessage(Update update) {

        List<List<InlineKeyboardButton>> startOptionButtons = List.of(
                List.of(InlineKeyboardButton.builder()
                        .text("Start!")
                        .url("https://oneune.duckdns.org:8081/test")
//                        .webApp(WebAppInfo.builder().url(Constants.URL.getField()).build())
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
}
