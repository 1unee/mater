package com.oneune.mater.rest.main.store.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Getter
public enum CommandEnum {

    START(UpdateTypeEnum.COMMAND.getPrefix() + "start", "Начать выбирать машину"),
    CONTACT_SELLER(UpdateTypeEnum.COMMAND.getPrefix() + "contact_seller", "Связаться с продавцом"),
    CONTACT_DEVELOPER(UpdateTypeEnum.COMMAND.getPrefix() + "contact_developer", "Связаться с разработчиком бота"),
    CARS_MENU(UpdateTypeEnum.COMMAND.getPrefix() + "cars_menu", "Главное меню с машинами"),
    NOTIFICATIONS_MENU(UpdateTypeEnum.COMMAND.getPrefix() + "notifications_menu", "Меню уведомлений об изменениях"),
    TEST(UpdateTypeEnum.COMMAND.getPrefix() + "test", "Test");

    String value;
    String description;
}
