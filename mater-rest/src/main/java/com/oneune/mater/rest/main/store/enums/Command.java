package com.oneune.mater.rest.main.store.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Getter
public enum Command {

    START(UpdateType.COMMAND.getPrefix() + "start", "Начать выбирать машину"),
    CONTACT_SELLER(UpdateType.COMMAND.getPrefix() + "contact_seller", "Связаться с продавцом"),
    CONTACT_DEVELOPER(UpdateType.COMMAND.getPrefix() + "contact_developer", "Связаться с разработчиком бота"),
    CARS_MENU(UpdateType.COMMAND.getPrefix() + "cars_menu", "Главное меню с машинами"),
    NOTIFICATIONS_MENU(UpdateType.COMMAND.getPrefix() + "notifications_menu", "Меню уведомлений об изменениях"),
    TEST(UpdateType.COMMAND.getPrefix() + "test", "Test");

    String value;
    String description;
}
