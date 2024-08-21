package com.oneune.mater.rest.bot.utils;

import com.oneune.mater.rest.main.store.enums.UpdateType;
import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.Update;

@UtilityClass
public final class DispatcherUtils {

    public UpdateType classifyUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            if (messageText.startsWith(UpdateType.COMMAND.getPrefix())) {
                return UpdateType.COMMAND;
            } else {
                return UpdateType.UNKNOWN;
            }
        } else {
            return UpdateType.UNKNOWN;
        }
    }
}
