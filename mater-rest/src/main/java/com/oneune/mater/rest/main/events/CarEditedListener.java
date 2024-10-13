package com.oneune.mater.rest.main.events;

import com.oneune.mater.rest.bot.configs.properties.TelegramBotProperties;
import com.oneune.mater.rest.main.readers.CarReader;
import com.oneune.mater.rest.main.services.NotificationService;
import com.oneune.mater.rest.main.services.TelegramService;
import com.oneune.mater.rest.main.store.entities.CarEntity;
import com.oneune.mater.rest.main.store.entities.UserEntity;
import com.oneune.mater.rest.main.store.entities.settings.OptionEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Triplet;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class CarEditedListener {

    private final static String FOREIGN_LINK_TEMPLATE = "Машина %s изменена.\nСсылка: %s";

    TelegramBotProperties telegramBotProperties;
    CarReader carReader;
    TelegramService telegramService;
    NotificationService notificationService;

    @EventListener
    public void handleCarUpdatedEvent(CarUpdatedEvent event) {

        List<Triplet<UserEntity, CarEntity, OptionEntity>> list = carReader.getFavoriteCarsForUpdateEvent(event.getCarId()).stream()
                .map(tuple -> Triplet.with(
                        tuple.get(0, UserEntity.class),
                        tuple.get(1, CarEntity.class),
                        tuple.get(2, OptionEntity.class))).toList();
        list.forEach(triplet -> sendNotificationByStrategy(
                triplet.getValue0(),
                triplet.getValue1(),
                triplet.getValue2().getValue()
        ));

        String s = "0;00";
    }

    private String getMessage(CarEntity carEntity) {
        String foreignLink = "%s/cars/market?car-id=%s".formatted(
                telegramBotProperties.getMenu().getButton().getUrl(), carEntity.getId()
        );
//        return FOREIGN_LINK_TEMPLATE.formatted(carEntity.getTitle(), foreignLink);
        return "Данные о машине %s изменились. Чтобы к ней перейти скопируйте «%s» (без кавычек) и вставьте в поиске."
                .formatted(carEntity.getTitle(), carEntity.getTitle());
    }

    private void notificateByTelegramChat(UserEntity userEntity, CarEntity carEntity) {
        telegramService.sendMessageAboutCarEdited(userEntity.getTelegramChatId(), getMessage(carEntity));
    }

    private void notificateByMail(UserEntity userEntity, CarEntity carEntity) {
        notificationService.sendSimpleMailToUser(
                userEntity.getEmail(),
                "Машина обновлена",
                getMessage(carEntity)
        );
    }

    private void sendNotificationByStrategy(UserEntity userEntity, CarEntity carEntity, String strategy) {
        if (strategy.equals("TELEGRAM_CHAT")) {
            notificateByTelegramChat(userEntity, carEntity);
        } else if (strategy.equals("MAIL")) {
            notificateByMail(userEntity, carEntity);
        } else if (strategy.equals("ALL")) {
            notificateByTelegramChat(userEntity, carEntity);
            notificateByMail(userEntity, carEntity);
        } else {
            throw new IllegalArgumentException("Unknown strategy: " + strategy);
        }
    }
}
