package com.oneune.mater.rest.bot.dispatchers;

import com.oneune.mater.rest.bot.contracts.Dispatcher;
import com.oneune.mater.rest.services.BotService;
import com.oneune.mater.rest.services.CarService;
import com.oneune.mater.rest.services.ContactService;
import com.oneune.mater.rest.services.NotificationService;
import com.oneune.mater.rest.store.enums.Command;
import com.oneune.mater.rest.store.exceptions.BusinessLogicException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class CommandDispatcher implements Dispatcher {

    BotService botService;
    CarService carService;
    NotificationService notificationService;
    ContactService contactService;

    @Override
    public void distribute(DefaultAbsSender bot, Update update) {

        String text = update.getMessage().getText();
        Command command = Arrays.stream(Command.values())
                .filter(cmd -> cmd.getValue().equals(text))
                .findFirst()
                .orElseThrow(() -> new BusinessLogicException("Not found command like <%s>".formatted(text)));

        switch (command) {
            case START -> botService.execute(bot, update);
            case CARS_MENU -> carService.execute(bot, update);
            case NOTIFICATIONS_MENU -> notificationService.execute(bot, update);
            case CONTACT_SELLER, CONTACT_DEVELOPER -> contactService.execute(bot, update);
            default -> throw new IllegalArgumentException("Unknown CommandType enum constant!");
        }
    }
}
