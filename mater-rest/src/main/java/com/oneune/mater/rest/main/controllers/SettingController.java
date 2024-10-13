package com.oneune.mater.rest.main.controllers;

import com.oneune.mater.rest.main.contracts.CRUDable;
import com.oneune.mater.rest.main.services.SettingService;
import com.oneune.mater.rest.main.store.dtos.settings.UserSettingsDto;
import com.oneune.mater.rest.main.store.entities.settings.UserSettingsEntity;
import com.oneune.mater.rest.main.store.pagination.PageQuery;
import com.oneune.mater.rest.main.store.pagination.PageResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("settings")
@CrossOrigin("*")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SettingController implements CRUDable<UserSettingsDto, UserSettingsEntity> {

    SettingService settingService;

    @GetMapping("users/{user-id}")
    public UserSettingsDto getByUserId(@PathVariable("user-id") Long userId) {
        return settingService.getByUserId(userId);
    }

    @PostMapping
    @Override
    public UserSettingsDto post(@RequestBody UserSettingsDto settings) {
        return settingService.post(settings);
    }

    @PutMapping("{settings-id}")
    @Override
    public UserSettingsDto put(@PathVariable(name = "settings-id") Long settingsId,
                               @RequestBody UserSettingsDto settings) {
        return settingService.put(settingsId, settings);
    }

    @DeleteMapping("{settings-id}")
    @Override
    public UserSettingsDto deleteById(@PathVariable(name = "settings-id") Long settingsId) {
        return settingService.deleteById(settingsId);
    }

    @GetMapping("{settings-id}")
    @Override
    public UserSettingsDto getById(@PathVariable(name = "settings-id") Long settingsId) {
        return settingService.getById(settingsId);
    }

    @PostMapping("search")
    @Override
    public PageResponse<UserSettingsDto> search(@RequestBody PageQuery pageQuery) {
        return settingService.search(pageQuery);
    }
}
