package com.oneune.mater.rest.main.configs;

import com.oneune.mater.rest.common.aop.annotations.ConfigurationBeansInfo;
import com.oneune.mater.rest.main.mappers.oneune.QueryDslModelMapperFactory;
import com.oneune.mater.rest.main.store.dtos.CarDto;
import com.oneune.mater.rest.main.store.dtos.RoleDto;
import com.oneune.mater.rest.main.store.dtos.UserDto;
import com.oneune.mater.rest.main.store.dtos.settings.ConfigDto;
import com.oneune.mater.rest.main.store.dtos.settings.OptionDto;
import com.oneune.mater.rest.main.store.dtos.settings.SettingDto;
import com.oneune.mater.rest.main.store.dtos.settings.UserSettingsDto;
import com.oneune.mater.rest.main.store.entities.CarEntity;
import com.oneune.mater.rest.main.store.entities.UserEntity;
import com.oneune.mater.rest.main.store.entities.UserRoleLinkEntity;
import com.oneune.mater.rest.main.store.entities.settings.OptionEntity;
import com.oneune.mater.rest.main.store.entities.settings.UserConfigLinkEntity;
import com.oneune.mater.rest.main.store.entities.settings.UserSettingLinkEntity;
import com.oneune.mater.rest.main.store.entities.settings.UserSettingsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.modelmapper.Conditions;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@ConfigurationBeansInfo
public class SerializationConfig {

    private ModelMapper modelMapper;

    private ModelMapper copyModelMapper() {
        return modelMapper.map(modelMapper, ModelMapper.class);
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(AccessLevel.PRIVATE);
        this.modelMapper = modelMapper;
        return modelMapper;
    }

    @Bean
    public QueryDslModelMapperFactory queryDslModelMapperFactory(ModelMapper modelMapper) {
        return new QueryDslModelMapperFactory(modelMapper);
    }

    @Bean
    public ModelMapper userModelMapper() {
        ModelMapper modelMapper = modelMapper();

        Converter<List<UserRoleLinkEntity>, List<RoleDto>> roleConverter = ctx -> ctx.getSource().stream()
                        .map(UserRoleLinkEntity::getRole)
                        .map(roleEntity -> modelMapper.map(roleEntity, RoleDto.class))
                        .collect(Collectors.toList());

        modelMapper.addMappings(new PropertyMap<UserEntity, UserDto>() {
            @Override
            protected void configure() {
                using(roleConverter).map(source.getUserRoleLinks()).setRoles(null);
            }
        });

        return modelMapper;
    }

    @Mapper(componentModel = "spring")
    public interface SettingsMapper {

        @Mapping(target = "configs", source = "userConfigLinks")
        @Mapping(target = "settings", source = "userSettingLinks")
        UserSettingsDto toDto(UserSettingsEntity settingsEntity);

        @Mapping(target = "selectedOption", source = "selectedOption")
        @Mapping(target = "options", source = "setting.options")
        @Mapping(target = "code", source = "setting.code")
        @Mapping(target = "title", source = "setting.title")
        @Mapping(target = "value", source = "setting.value")
        @Mapping(target = "description", source = "setting.description")
        SettingDto userSettingLinkToSettingDto(UserSettingLinkEntity userSettingLinkEntity);

        @Mapping(target = "code", source = "config.code")
        @Mapping(target = "title", source = "config.title")
        @Mapping(target = "value", source = "config.value")
        @Mapping(target = "description", source = "config.description")
        ConfigDto toDto(UserConfigLinkEntity userConfigLinkEntity);

        default List<SettingDto> toSettingDtos(List<UserSettingLinkEntity> userSettingLinkEntities) {
            return userSettingLinkEntities.stream()
                    .map(this::userSettingLinkToSettingDto)
                    .collect(Collectors.toList());
        }

        default List<ConfigDto> toConfigDtos(List<UserConfigLinkEntity> userConfigLinkEntities) {
            return userConfigLinkEntities.stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
        }

        @Mapping(target = "code", source = "code")
        @Mapping(target = "title", source = "title")
        @Mapping(target = "value", source = "value")
        @Mapping(target = "description", source = "description")
        OptionDto toDto(OptionEntity optionEntity);

        default List<UserSettingsDto> toDtoList(List<UserSettingsEntity> settingsEntities) {
            return settingsEntities.stream().map(this::toDto).collect(Collectors.toList());
        }
    }
}
