package com.oneune.mater.rest.main.controllers;

import com.oneune.mater.rest.main.contracts.CRUDable;
import com.oneune.mater.rest.main.services.UserService;
import com.oneune.mater.rest.main.store.dtos.UserDto;
import com.oneune.mater.rest.main.store.dtos.UserTokenDto;
import com.oneune.mater.rest.main.store.entities.UserEntity;
import com.oneune.mater.rest.main.store.enums.VariableFieldEnum;
import com.oneune.mater.rest.main.store.pagination.PageQuery;
import com.oneune.mater.rest.main.store.pagination.PageResponse;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

@RestController
@RequestMapping("users")
@CrossOrigin("*")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserController implements CRUDable<UserDto, UserEntity> {

    UserService userService;

    @PostMapping
    @Override
    public UserDto post(@RequestBody @Nullable UserDto user) {
        return userService.post(user);
    }

    @PostMapping("by-telegram-user")
    public UserDto registerOrGet(@RequestBody User telegramUser,
                                 @RequestParam(name = "telegram-chat-id") String telegramChatId,
                                 @RequestParam(required = false, defaultValue = "") List<String> additionalRoles) {
        return this.userService.registerOrGet(telegramUser, telegramChatId, additionalRoles);
    }

    @PutMapping("by-telegram-user")
    public UserDto putByTelegramUser(@RequestBody User telegramUser,
                                     @RequestParam(name = "user-id") Long userId,
                                     @RequestParam(name = "token") Integer token,
                                     @RequestParam(name = "telegram-chat-id") String telegramChatId,
                                     @RequestParam(required = false, defaultValue = "") List<String> additionalRoles) {
        return this.userService.putByTelegramUser(userId, token, telegramUser, telegramChatId, additionalRoles);
    }

    @PutMapping("{id}")
    @Override
    public UserDto put(@PathVariable(name = "id") Long userId,
                       @RequestBody UserDto user) {
        return userService.put(userId, user);
    }

    @PutMapping("{id}/fields/{enum}")
    public UserDto edit(@PathVariable(name = "id") Long userId,
                        @PathVariable(name = "enum")  VariableFieldEnum variableField,
                        @RequestBody UserDto user) {
        return userService.putByParams(userId, user, variableField);
    }

    @DeleteMapping("{id}")
    @Override
    public UserDto deleteById(@PathVariable(name = "id") Long userId) {
        return userService.deleteById(userId);
    }

    @GetMapping("{id}")
    @Override
    public UserDto getById(@PathVariable(name = "id") Long userId) {
        return userService.getById(userId);
    }

    @PostMapping("search")
    @Override
    public PageResponse<UserDto> search(@RequestBody PageQuery pageQuery) {
        return userService.search(pageQuery);
    }

    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("{user-id}/token")
    public UserTokenDto getUserToken(@PathVariable(name = "user-id") Long userId) {
        return userService.getUserToken(userId);
    }
}
