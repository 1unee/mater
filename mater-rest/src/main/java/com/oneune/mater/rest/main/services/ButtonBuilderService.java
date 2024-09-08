package com.oneune.mater.rest.main.services;

import com.oneune.mater.rest.bot.configs.properties.TelegramBotProperties;
import com.oneune.mater.rest.bot.utils.TelegramBotUtils;
import com.oneune.mater.rest.bot.utils.TextWrapperUtils;
import com.oneune.mater.rest.main.store.enums.Constants;
import com.oneune.mater.rest.main.store.enums.TextWrapperType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class ButtonBuilderService {

    private final static String DEFAULT_KEYBOARD_BUTTONS_MESSAGE = "Выбери нужную опцию ниже";

    TelegramBotProperties telegramBotProperties;

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

    public SendMessage buildStartMessageWithOnlyText(Update update) {
         String buttonTitle = telegramBotProperties.getMenu().getButton().getTitle();
        return SendMessage.builder()
                .chatId(TelegramBotUtils.extractChatId(update).toString())
                .parseMode(TextWrapperType.HTML.getDependencyKind())
                .text("""
                      Как  начать работу со мной:
                      
                      1) слева снизу есть кнопка %s - нажать на нее
                      2) готово - вы прекрасны
                      """.formatted(TextWrapperUtils.wrapHtml(buttonTitle).bold().underlined().complete()))
                .build();
    }
}
