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
import com.oneune.mater.rest.main.store.enums.UserRegistrationState;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class UserService implements CRUDable<UserDto, UserEntity> {

    ModelMapper userModelMapper;
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
        if (userReader.getByUsername(userDto.getUsername()).isPresent()) {
            throw new BusinessLogicException("Такой логин уже занят!");
        } else {
            UserEntity userEntity = UserEntity.builder()
                    .username(userDto.getUsername())
                    .password(userDto.getPassword())
                    .status(UserRegistrationState.MANUALLY)
                    .email(userDto.getEmail())
                    .personal(new PersonalEntity())
                    .seller(new SellerEntity())
                    .build();
            userEntity.setUserRoleLinks(linkRoles(userEntity, RoleEnum.USER));
            userRepository.saveAndFlush(userEntity);
            settingService.postDefault(userEntity);
            return userModelMapper.map(userEntity, UserDto.class);
        }
    }

    @Transactional
    public void register(DefaultAbsSender bot, Update update, String roleName) {
        User telegramUser = update.getMessage().getFrom();
        registerOrGet(telegramUser, update.getMessage().getChatId().toString(), List.of(RoleEnum.valueOf(roleName)));
        TelegramBotUtils.informAboutSuccess(bot, update);
    }

    /**
     * @param additionalRoles roles to add for user (exclude USER role which set by default).
     */
    @Transactional
    public UserDto registerOrGet(User telegramUser, String telegramChatId, List<RoleEnum> additionalRoles) {
        Optional<UserDto> user = userReader.getByUsername(telegramUser.getUserName());
        return user.orElseGet(() -> register(
                telegramUser,
                telegramChatId.equals("undefined") ? null : Long.parseLong(telegramChatId), additionalRoles
        ));
    }

    @Transactional
    protected UserDto register(User telegramUser, @Nullable Long telegramChatId, List<RoleEnum> additionalRoles) {
        PersonalEntity personalEntity = personalService.postByTelegramUser(telegramUser,null,null);
        SellerEntity sellerEntity = sellerService.post(telegramUser.getUserName());
        UserEntity userEntity = buildUserByTelegram(
                telegramUser, telegramChatId, null, personalEntity, sellerEntity, additionalRoles
        );
        userRepository.saveAndFlush(userEntity);
        settingService.postDefault(userEntity);
        return userReader.getById(userEntity.getId());
    }

    private UserEntity buildUserByTelegram(User telegramUser,
                                           @Nullable Long telegramChatId,
                                           @Nullable @Email String email,
                                           PersonalEntity personalEntity,
                                           SellerEntity sellerEntity,
                                           List<RoleEnum> roles) {
        UserEntity userEntity = UserEntity.builder()
                .username(Optional.ofNullable(telegramUser.getUserName())
                        .orElse("user_%s".formatted(telegramUser.getId())))
                .isUsernameSet(true)
                .password(String.valueOf(Objects.hashCode(Instant.now())))
                .email(email)
                .telegramId(telegramUser.getId())
                .telegramChatId(telegramChatId)
                .registeredAt(Instant.now())
                .status(UserRegistrationState.BY_TELEGRAM)
                .personal(personalEntity)
                .seller(sellerEntity)
                .build();
        userEntity.setUserRoleLinks(linkRoles(userEntity, roles.toArray(new RoleEnum[0])));
        return userEntity;
    }

    /**
     * By default, always sets USER role.
     */
    private List<UserRoleLinkEntity> linkRoles(UserEntity userEntity, RoleEnum ...roles) {
        Stream<RoleEnum> rolesWithoutUserRole = Arrays.stream(roles)
                .filter(role -> !role.equals(RoleEnum.USER));
        return Stream.concat(Stream.of(RoleEnum.USER), rolesWithoutUserRole)
                .map(roleService::getEntityByEnum)
                .map(roleEntity -> UserRoleLinkEntity.builder()
                        .user(userEntity)
                        .role(roleEntity)
                        .build())
                .map(wildcard -> (UserRoleLinkEntity) wildcard)
                .toList();
    }

    public UserDto login(String username, String password) {
        Optional<UserDto> optUser = userReader.getByUsername(username);
        if (optUser.isPresent() && optUser.get().getPassword().equals(password)) {
            return optUser.get();
        } else {
            throw new BusinessLogicException("Ошибка либо в логине, либо в пароле.");
        }
    }

    @Transactional
    @Override
    public UserDto put(Long userId, UserDto userDto) {
        UserEntity userEntity = userReader.getEntityById(userId);
        userModelMapper.map(userDto, userEntity);
        roleService.linkRoles(userEntity, userDto.getRoles());
        userRepository.saveAndFlush(userEntity);
        return userReader.getById(userDto.getId());
    }

    @Transactional
    public UserDto putByParams(Long userId, UserDto userDto, VariableFieldEnum variableField) {
        UserEntity userEntity = userReader.getEntityById(userId);
        userModelMapper.map(userDto, userEntity);
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
        return userModelMapper.map(userEntity, UserDto.class);
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
            return userModelMapper.map(tokenEntity.get(), UserTokenDto.class);
        } else {
            throw new BusinessLogicException("User token not created yet!");
        }
    }

    public UserDto putByTelegramUser(Long userId,
                                     Integer token,
                                     User telegramUser,
                                     String telegramChatId,
                                     List<RoleEnum> additionalRoles) {
        UserEntity userEntity = getEntityById(userId);
        userEntity.getUserRoleLinks().addAll(linkRoles(userEntity, additionalRoles.toArray(new RoleEnum[0])));
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
