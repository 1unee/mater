package com.oneune.mater.rest.main.services;

import com.oneune.mater.rest.bot.utils.TelegramBotUtils;
import com.oneune.mater.rest.main.contracts.CRUDable;
import com.oneune.mater.rest.main.readers.UserReader;
import com.oneune.mater.rest.main.repositories.UserRepository;
import com.oneune.mater.rest.main.repositories.UserTokenRepository;
import com.oneune.mater.rest.main.store.dtos.UserDto;
import com.oneune.mater.rest.main.store.dtos.UserTokenDto;
import com.oneune.mater.rest.main.store.entities.*;
import com.oneune.mater.rest.main.store.enums.RoleEnum;
import com.oneune.mater.rest.main.store.enums.VariableFieldEnum;
import com.oneune.mater.rest.main.store.exceptions.BusinessLogicException;
import com.oneune.mater.rest.main.store.pagination.PageQuery;
import com.oneune.mater.rest.main.store.pagination.PageResponse;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class UserService implements CRUDable<UserDto, UserEntity> {

    ModelMapper modelMapper;
    UserRepository userRepository;
    UserReader userReader;
    PersonalService personalService;
    RoleService roleService;
    SellerService sellerService;
    SettingService settingService;
    UserTokenRepository userTokenRepository;

    @Transactional
    @Override
    public UserDto post(UserDto userDto) {
        if (Objects.isNull(userDto)) {
            return registerByForeignLink();
        } else {
            UserEntity userEntity = new UserEntity();
            modelMapper.map(userDto, userEntity);
            userEntity.setRegisteredByTelegram(false);
            userRepository.saveAndFlush(userEntity);
        }
        return userReader.getById(userDto.getId());
    }

    @Transactional
    public void register(DefaultAbsSender bot, Update update, String roleName) {
        User telegramUser = update.getMessage().getFrom();
        UserDto userDto = registerOrGet(telegramUser, update.getMessage().getChatId().toString(), List.of(roleName));
        TelegramBotUtils.informAboutSuccess(bot, update);
    }

    /**
     * @param additionalRoles roles to add for user (exclude USER role which set by default).
     */
    @Transactional
    public UserDto registerOrGet(User telegramUser, String telegramChatId, List<String> additionalRoles) {
        Optional<UserDto> user = userReader.getByUsername(telegramUser.getUserName());
        return user.orElseGet(() -> register(telegramUser, telegramChatId.equals("undefined") ? null : Long.parseLong(telegramChatId), additionalRoles));
    }

    @Transactional
    protected UserDto register(User telegramUser, @Nullable Long telegramChatId, List<String> additionalRoles) {
        RoleEntity roleEntity = roleService.getEntityByEnum(RoleEnum.USER);
        PersonalEntity personalEntity = personalService.postByTelegramUser(telegramUser,null,null);
        SellerEntity sellerEntity = sellerService.post(telegramUser.getUserName());
        UserEntity userEntity = buildUserByTelegramUser(
                telegramUser, telegramChatId, null, personalEntity, sellerEntity,
                Stream.concat(Stream.of(roleEntity), additionalRoles.stream().map(roleService::getEntityByEnum)).toList()
        );
        userRepository.saveAndFlush(userEntity);
        settingService.postDefault(userEntity);
        return userReader.getById(userEntity.getId());
    }

    private UserEntity buildUserByTelegramUser(User telegramUser,
                                               @Nullable Long telegramChatId,
                                               @Nullable @Email String email,
                                               PersonalEntity personalEntity,
                                               SellerEntity sellerEntity,
                                               List<RoleEntity> roleEntities) {
        List<UserRoleLinkEntity> userRoleLinkEntities = roleEntities.stream()
                .map(roleEntity -> (UserRoleLinkEntity) UserRoleLinkEntity.builder()
                        .role(roleEntity)
                        .build())
                .toList();
        UserEntity userEntity = UserEntity.builder()
                .username(Optional.ofNullable(telegramUser.getUserName())
                        .orElse("user_%s".formatted(telegramUser.getId())))
                .isUsernameSet(true)
                .email(email)
                .registeredByTelegram(true)
                .telegramId(telegramUser.getId())
                .telegramChatId(telegramChatId)
                .registeredAt(Instant.now())
                .personal(personalEntity)
                .seller(sellerEntity)
                .userRoleLinks(userRoleLinkEntities)
                .build();
        userRoleLinkEntities.forEach(userRoleLinkEntity -> userRoleLinkEntity.setUser(userEntity));
        return userEntity;
    }

    @Transactional
    public UserDto registerByForeignLink() {
        UserEntity userEntity = UserEntity.builder()
                .username("user-" + UUID.randomUUID())
                .isUsernameSet(false)
                .registeredAt(Instant.now())
                .personal(PersonalEntity.builder().build())
                .seller(SellerEntity.builder().build())
                .registeredByTelegram(false)
                .build();

        UserRoleLinkEntity userRoleLinkEntity = UserRoleLinkEntity.builder()
                .user(userEntity)
                .role(roleService.getEntityByEnum(RoleEnum.USER))
                .build();
        userEntity.setUserRoleLinks(List.of(userRoleLinkEntity));
        userRepository.saveAndFlush(userEntity);

        UserTokenEntity userTokenEntity = UserTokenEntity.builder()
                .user(userEntity)
                .value(UserTokenEntity.getUniqueValue())
                .build();
        userTokenRepository.save(userTokenEntity);

        settingService.postDefault(userEntity);
        return userReader.getById(userEntity.getId());
    }

    @Transactional
    @Override
    public UserDto put(Long userId, UserDto userDto) {
        UserEntity userEntity = userReader.getEntityById(userId);
        modelMapper.map(userDto, userEntity);
        roleService.linkRoles(userEntity, userDto.getRoles());
        userRepository.saveAndFlush(userEntity);
        return userReader.getById(userDto.getId());
    }

    @Transactional
    public UserDto putByParams(Long userId, UserDto userDto, VariableFieldEnum variableField) {
        UserEntity userEntity = userReader.getEntityById(userId);
        modelMapper.map(userDto, userEntity);
        roleService.linkRoles(userEntity, userDto.getRoles());
        commitVariableField(userEntity, variableField);
        userRepository.saveAndFlush(userEntity);
        return userReader.getById(userDto.getId());
    }

    private void commitVariableField(@NonFinal UserEntity userEntity,
                                     VariableFieldEnum variableField) {
        switch (variableField) {
            case USERNAME -> userEntity.setUsernameSet(true);
            case EMAIL -> userEntity.setEmailSet(true);
            case FIRST_NAME -> userEntity.getPersonal().setFirstNameSet(true);
            case LAST_NAME -> userEntity.getPersonal().setLastNameSet(true);
            case MIDDLE_NAME -> userEntity.getPersonal().setMiddleNameSet(true);
            case BIRTH_DATE -> userEntity.getPersonal().setBirthDateSet(true);
            default -> throw new IllegalStateException("Unexpected value: " + variableField);
        }
    }

    @Transactional
    @Override
    public UserDto deleteById(Long userId) {
        UserEntity userEntity = userReader.getEntityById(userId);
        userRepository.delete(userEntity);
        userRepository.flush();
        return modelMapper.map(userEntity, UserDto.class);
    }

    public UserEntity getEntityById(Long userId) {
        return userReader.getEntityById(userId);
    }

    @Override
    public UserDto getById(Long userId) {
        return userReader.getById(userId);
    }

    @Override
    public PageResponse<UserDto> search(PageQuery pageQuery) {
        return userReader.search(pageQuery);
    }

    public List<UserDto> getUsers() {
        return userReader.getUsers();
    }

    public UserTokenDto getUserToken(Long userId) {
        UserEntity userEntity = getEntityById(userId);
        Optional<UserTokenEntity> tokenEntity = userTokenRepository.findByUser(userEntity);
        if (tokenEntity.isPresent()) {
            return modelMapper.map(tokenEntity.get(), UserTokenDto.class);
        } else {
            throw new BusinessLogicException("User token not created yet!");
        }
    }

    public UserDto putByTelegramUser(Long userId,
                                     Integer token,
                                     User telegramUser,
                                     String telegramChatId,
                                     List<String> additionalRoles) {
        UserEntity userEntity = getEntityById(userId);
        Optional<UserTokenEntity> optOriginalToken = userTokenRepository.findByUser(userEntity);
        Optional<UserTokenEntity> optProvidedToken = userTokenRepository.findByValue(token);
        if (optOriginalToken.isEmpty() || optProvidedToken.isEmpty()) {
            throw new BusinessLogicException("Такого токена не существует...");
        } else {
            if (optOriginalToken.get().getValue().equals(optProvidedToken.get().getValue())) {
                userEntity.setUsername(telegramUser.getUserName());
                userEntity.setUsernameSet(true);
                userEntity.setTelegramId(telegramUser.getId());
                userEntity.setTelegramChatId(telegramChatId.equals("undefined") ? null : Long.parseLong(telegramChatId));
                userRepository.saveAndFlush(userEntity);
                return getById(userEntity.getId());
            } else {
                throw new BusinessLogicException("Неверное значение токена!");
            }
        }
    }
}
