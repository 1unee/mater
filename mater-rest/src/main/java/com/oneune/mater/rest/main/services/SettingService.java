package com.oneune.mater.rest.main.services;

import com.oneune.mater.rest.main.contracts.CRUDable;
import com.oneune.mater.rest.main.readers.SettingReader;
import com.oneune.mater.rest.main.repositories.settings.SettingsRepository;
import com.oneune.mater.rest.main.store.dtos.settings.SettingDto;
import com.oneune.mater.rest.main.store.dtos.settings.UserSettingsDto;
import com.oneune.mater.rest.main.store.entities.UserEntity;
import com.oneune.mater.rest.main.store.entities.settings.*;
import com.oneune.mater.rest.main.store.pagination.PageQuery;
import com.oneune.mater.rest.main.store.pagination.PageResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class SettingService implements CRUDable<UserSettingsDto, UserSettingsEntity> {

    SettingsRepository settingsRepository;
    SettingReader settingReader;

    @Transactional
    public UserSettingsDto postDefault(UserEntity userEntity) {

        UserSettingsEntity settingsEntity = UserSettingsEntity.builder()
                .user(userEntity)
                .build();

        List<UserConfigLinkEntity> userConfigLinkEntities = settingReader.getAllConfigEntities().stream()
                .map(configEntity -> UserConfigLinkEntity.builder()
                        .userSettings(settingsEntity)
                        .config(configEntity)
                        .build())
                .map(wildCard -> (UserConfigLinkEntity) wildCard)
                .toList();

        List<UserSettingLinkEntity> userSettingLinkEntities = settingReader.getAllSettingEntities().stream()
                .map(settingEntity -> UserSettingLinkEntity.builder()
                        .userSettings(settingsEntity)
                        .setting(settingEntity)
                        .selectedOption(settingEntity.getOptions().get(0))
                        .build())
                .map(wildCard -> (UserSettingLinkEntity) wildCard)
                .toList();

        settingsEntity.setUserConfigLinks(userConfigLinkEntities);
        settingsEntity.setUserSettingLinks(userSettingLinkEntities);
        settingsRepository.save(settingsEntity);
        return settingReader.getByUserId(userEntity.getId());
    }

    public UserSettingsDto getByUserId(Long userId) {
        return settingReader.getByUserId(userId);
    }

    @Override
    public UserSettingsDto post(UserSettingsDto userSettingsDto) {
        throw new IllegalStateException("Not implemented yet!");
    }

    @Transactional
    @Override
    public UserSettingsDto put(Long settingsId, UserSettingsDto userSettingsDto) {
        UserSettingsEntity settingsEntity = settingReader.getEntityById(settingsId);

        Map<Integer, SettingDto> settingsMap = userSettingsDto.getSettings().stream()
                .collect(Collectors.toMap(SettingDto::getCode, Function.identity()));

        Map<Integer, OptionEntity> optionsMap = settingReader.getAllSettingEntities().stream()
                .map(SettingEntity::getOptions)
                .flatMap(List::stream)
                .collect(Collectors.toMap(OptionEntity::getCode, Function.identity()));

        settingsEntity.getUserSettingLinks().stream()
                .map(settingLink -> Pair.of(settingLink, settingsMap.get(settingLink.getSetting().getCode())))
                .filter(pair -> !pair.getFirst().getSelectedOption().getId().equals(pair.getSecond().getSelectedOption().getId()))
                .forEach(pair -> {
                    UserSettingLinkEntity userSettingLinkEntity = pair.getFirst();
                    SettingDto settingDto = pair.getSecond();
                    OptionEntity changedOptionEntity = optionsMap.get(settingDto.getSelectedOption().getCode());
                    userSettingLinkEntity.setSelectedOption(changedOptionEntity);
                    userSettingLinkEntity.setUpdatedAt(Instant.now());
        });

        settingsEntity.setUpdatedAt(Instant.now());
        settingsRepository.saveAndFlush(settingsEntity);
        return settingReader.getById(settingsId);
    }

    @Override
    public UserSettingsDto deleteById(Long settingsId) {
        throw new IllegalStateException("Not implemented yet!");
    }

    @Override
    public UserSettingsDto getById(Long settingsId) {
        throw new IllegalStateException("Not implemented yet!");
    }

    @Override
    public PageResponse<UserSettingsDto> search(PageQuery pageQuery) {
        throw new IllegalStateException("Not implemented yet!");
    }

    public List<UserSettingsDto> getAllSettings() {
        return settingReader.getAllSettings();
    }
}
