package com.oneune.mater.rest.bot.dispatchers;

import com.oneune.mater.rest.bot.contracts.Dispatcher;
import com.oneune.mater.rest.bot.utils.SerializationUtils;
import com.oneune.mater.rest.main.services.UserService;
import com.oneune.mater.rest.main.store.dtos.WebAppDataDto;
import com.oneune.mater.rest.main.store.enums.WebAppDataEnum;
import com.oneune.mater.rest.main.store.exceptions.BusinessLogicException;
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
public class WebAppDataDispatcher implements Dispatcher {

    UserService userService;

    @Override
    public void distribute(DefaultAbsSender bot, Update update) {

        WebAppDataDto<?> webAppData = SerializationUtils.fromJson(
                update.getMessage().getWebAppData().getData(),
                WebAppDataDto.class
        );

        WebAppDataEnum webAppDataConst = Arrays.stream(WebAppDataEnum.values())
                .filter(data -> data.getValue().equals(webAppData.getType().getValue()))
                .findFirst()
                .orElseThrow(() -> new BusinessLogicException(
                        "Not found web app data constant like <%s>".formatted(webAppData.getType().getValue())
                ));

        switch (webAppDataConst) {
            case REGISTER_USER -> userService.registerUser(bot, update, (String) webAppData.getData());
            default -> throw new IllegalArgumentException("Unknown CommandType enum constant!");
        }
    }
}
