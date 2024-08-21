package com.oneune.mater.rest.main.services;

import com.oneune.mater.rest.bot.utils.TelegramBotUtils;
import com.oneune.mater.rest.main.repositories.PersonalRepository;
import com.oneune.mater.rest.main.repositories.UserRepository;
import com.oneune.mater.rest.main.store.entities.PersonalEntity;
import com.oneune.mater.rest.main.store.entities.RoleEntity;
import com.oneune.mater.rest.main.store.entities.UserEntity;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class UserService {

    UserRepository userRepository;
    PersonalRepository personalRepository;

    RoleService roleService;

    @Transactional
    public void registerUser(DefaultAbsSender bot, Update update, String roleName) {
        User telegramUser = update.getMessage().getFrom();
        RoleEntity roleEntity = roleService.get(roleName);
        PersonalEntity personalEntity = buildPersonal(telegramUser, null, null);
        personalRepository.saveAndFlush(personalEntity);
        UserEntity userEntity = buildUser(update, telegramUser, null, personalEntity, roleEntity);
        userRepository.saveAndFlush(userEntity);
        TelegramBotUtils.informAboutSuccess(bot, update);
    }

    public PersonalEntity buildPersonal(User telegramUser,
                                        @Nullable String middleName,
                                        @Nullable LocalDate birthDate) {
        return PersonalEntity.builder()
                .firstName(telegramUser.getFirstName())
                .lastName(telegramUser.getLastName())
                .middleName(middleName)
                .birthDate(birthDate)
                .build();
    }

    public UserEntity buildUser(Update update,
                                User telegramUser,
                                @Nullable @Email String email,
                                PersonalEntity personal,
                                RoleEntity roleEntity) {
        return UserEntity.builder()
                .username(telegramUser.getUserName())
                .email(email)
                .telegramId(telegramUser.getId())
                .registeredAt(Instant.ofEpochSecond(update.getMessage().getDate()))
                .personal(personal)
                .roles(List.of(roleEntity))
                .build();
    }
}
