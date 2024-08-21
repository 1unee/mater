package com.oneune.mater.rest.bot.utils;

import com.oneune.mater.rest.main.store.enums.UpdateTypeEnum;
import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

@UtilityClass
public final class DispatcherUtils {

    public UpdateTypeEnum classifyUpdate(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText() && message.getText().startsWith(UpdateTypeEnum.COMMAND.getPrefix())) {
                return UpdateTypeEnum.COMMAND;
            } else if (Objects.nonNull(message.getWebAppData())) {
                return UpdateTypeEnum.WEB_APP_DATA;
            } else {
                return UpdateTypeEnum.UNKNOWN;
            }
        } else {
            return UpdateTypeEnum.UNKNOWN;
        }
    }
}
