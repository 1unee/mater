package com.oneune.mater.rest.main.readers;

import com.google.gson.reflect.TypeToken;
import com.oneune.mater.rest.main.configs.SerializationConfig.SettingsMapper;
import com.oneune.mater.rest.main.contracts.BaseQueryable;
import com.oneune.mater.rest.main.contracts.Readable;
import com.oneune.mater.rest.main.store.dtos.settings.ConfigDto;
import com.oneune.mater.rest.main.store.dtos.settings.SettingDto;
import com.oneune.mater.rest.main.store.dtos.settings.UserSettingsDto;
import com.oneune.mater.rest.main.store.entities.settings.*;
import com.oneune.mater.rest.main.store.pagination.PageQuery;
import com.oneune.mater.rest.main.store.pagination.PageResponse;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Type;
import java.util.List;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SettingReader implements Readable<UserSettingsDto, UserSettingsEntity>, BaseQueryable<UserSettingsEntity> {

    public final static Type CONFIG_ITEM_LIST_TYPE = TypeToken.getParameterized(List.class, ConfigDto.class).getType();
    public final static Type SETTING_ITEM_LIST_TYPE = TypeToken.getParameterized(List.class, SettingDto.class).getType();
    public final static QUserSettingsEntity qUserSettings = new QUserSettingsEntity("user_settings");
    public final static QConfigEntity qConfig = new QConfigEntity("config");
    public final static QSettingEntity qSetting = new QSettingEntity("setting");
    public final static QOptionEntity qOption = new QOptionEntity("option");
    public final static QUserConfigLinkEntity qUserConfigLink = new QUserConfigLinkEntity("user_config_link");
    public final static QUserSettingLinkEntity qUserSettingLink = new QUserSettingLinkEntity("user_setting_link");

    JPAQueryFactory queryFactory;
    ModelMapper modelMapper;
    SettingsMapper settingsMapper;

    @Override
    public JPAQuery<UserSettingsEntity> writeBaseQuery(Predicate... predicates) {
        return queryFactory.selectFrom(qUserSettings).where(predicates)
                .join(qUserConfigLink).on(qUserConfigLink.userSettings.id.eq(qUserSettings.id)).fetchJoin()
                .join(qConfig).on(qUserConfigLink.config.id.eq(qConfig.id)).fetchJoin()
                .join(qUserSettingLink).on(qUserSettingLink.userSettings.id.eq(qUserSettings.id)).fetchJoin()
                .join(qSetting).on(qUserSettingLink.setting.id.eq(qSetting.id)).fetchJoin()
                .join(qOption).on(qOption.root.id.eq(qSetting.id)).fetchJoin()
                .orderBy(qSetting.id.asc());
    }

    public List<ConfigEntity> getAllConfigEntities() {
        return queryFactory.selectFrom(qConfig).fetch();
    }

    public List<SettingEntity> getAllSettingEntities() {
        return queryFactory.selectFrom(qSetting)
                .join(qOption).on(qOption.root.id.eq(qSetting.id)).fetchJoin()
                .fetch();
    }

    public UserSettingsDto getByUserId(Long userId) {
        UserSettingsEntity settingsEntity = writeBaseQuery(qUserSettings.user.id.eq(userId)).fetchOne();
        return settingsMapper.toDto(settingsEntity);
    }

    @Override
    public UserSettingsDto getById(Long settingsId) {
        return settingsMapper.toDto(writeBaseQuery(qUserSettings.id.eq(settingsId)).fetchOne());
    }

    @Override
    public PageResponse<UserSettingsDto> search(PageQuery pageQuery) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public UserSettingsEntity getEntityById(Long settingsId) {
        return writeBaseQuery(qUserSettings.id.eq(settingsId)).fetchOne();
    }

    public List<UserSettingsDto> getAllSettings() {
        return settingsMapper.toDtoList(writeBaseQuery().fetch());
    }
}
