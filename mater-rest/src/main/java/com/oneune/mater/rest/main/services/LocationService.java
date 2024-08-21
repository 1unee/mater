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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class LocationService implements Command {

    @Override
    public void execute(DefaultAbsSender bot, Update update) {
        TelegramBotUtils.informAboutDeveloping(bot, update);
    }

    public void handleLocationRequest(Update update, DefaultAbsSender bot) {

        KeyboardButton locationButton = KeyboardButton.builder()
                .text("Отправить геолокацию")
                .requestLocation(true)
                .build();

        KeyboardRow row = new KeyboardRow(List.of(locationButton));

        ReplyKeyboardMarkup keyboardMarkup = ReplyKeyboardMarkup.builder()
                .keyboard(List.of(row))
                .build();

        SendMessage message = SendMessage.builder()
                .chatId(TelegramBotUtils.extractChatId(update).toString())
                .text("Пожалуйста, отправьте свою геолокацию")
                .replyMarkup(keyboardMarkup)
                .build();

        TelegramBotUtils.uncheckedExecute(bot, message);
    }

    private void saveLocation(Message message) {

        /**
         * Latitude: 55.629886, Longitude: 37.856793
         * Latitude: 55.645737, Longitude: 37.649939
         * distanceService.calculate(new DistanceService.Location(55.629886, 37.856793), new DistanceService.Location(55.645737, 37.649939)) -> 13,1 км
         */

        if (message.hasLocation()) {
            double latitude = message.getLocation().getLatitude();
            double longitude = message.getLocation().getLongitude();
            log.info("Location received: Latitude: " + latitude + ", Longitude: " + longitude);

            // Здесь можно добавить код для сохранения геолокации в базу данных или другой хранилище
        } else {
            log.info("No location found in the message!");
        }
    }
}
