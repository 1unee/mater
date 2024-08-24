package com.oneune.mater.rest.main.services;

import com.oneune.mater.rest.bot.contracts.Command;
import com.oneune.mater.rest.bot.utils.TelegramBotUtils;
import com.oneune.mater.rest.main.readers.CarReader;
import com.oneune.mater.rest.main.store.dtos.CarDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class CarService implements Command {

    CarReader carReader;
    ButtonService buttonService;

    @Override
    public void execute(DefaultAbsSender bot, Update update) {
        SendMessage sendCarsMenuKeyboardButtons = buttonService.buildCarsKeyboardButtons(update);
        TelegramBotUtils.uncheckedExecute(bot, sendCarsMenuKeyboardButtons);
    }

    public List<CarDto> search(Integer page, Integer size) {
        return carReader.search(page, size);
    }
}
