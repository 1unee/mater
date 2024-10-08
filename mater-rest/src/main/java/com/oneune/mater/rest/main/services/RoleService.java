package com.oneune.mater.rest.main.services;

import com.oneune.mater.rest.main.contracts.CRUDable;
import com.oneune.mater.rest.main.readers.RoleReader;
import com.oneune.mater.rest.main.repositories.RoleRepository;
import com.oneune.mater.rest.main.repositories.UserRoleLinkRepository;
import com.oneune.mater.rest.main.store.entities.UserEntity;
import com.oneune.mater.rest.main.store.entities.UserRoleLinkEntity;
import com.oneune.mater.rest.main.store.pagination.PageQuery;
import com.oneune.mater.rest.main.store.pagination.PageResponse;
import com.oneune.mater.rest.main.store.dtos.RoleDto;
import com.oneune.mater.rest.main.store.entities.RoleEntity;
import com.oneune.mater.rest.main.store.enums.RoleEnum;
import com.oneune.mater.rest.main.store.exceptions.BusinessLogicException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class RoleService implements CRUDable<RoleDto, RoleEntity> {

    ModelMapper modelMapper;
    RoleRepository roleRepository;
    UserRoleLinkRepository userRoleLinkRepository;
    RoleReader roleReader;

    public RoleEntity getEntityByEnum(RoleEnum roleConst) {
        return roleReader.getEntityByEnum(roleConst);
    }

    public RoleEnum getConst(String roleName) {
        return Arrays.stream(RoleEnum.values())
                .filter(roleConstant -> roleConstant.getRus().equalsIgnoreCase(roleName))
                .findFirst()
                .orElseThrow(() -> new BusinessLogicException("Not found role const like <%s>".formatted(roleName)));
    }

    public RoleEntity getEntityByEnum(String roleName) {
        RoleEnum roleConst = getConst(roleName);
        return getEntityByEnum(roleConst);
    }

    @Override
    public RoleDto post(RoleDto roleDto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RoleDto put(Long roleId, RoleDto roleDto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RoleDto deleteById(Long roleId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public RoleEntity getEntityById(Long roleId) {
        return roleReader.getEntityById(roleId);
    }

    @Override
    public RoleDto getById(Long roleId) {
        return roleReader.getById(roleId);
    }

    @Override
    public PageResponse<RoleDto> search(PageQuery pageQuery) {
        return roleReader.search(pageQuery);
    }

    public List<RoleDto> getRoles() {
        return roleReader.getRoles();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void linkRoles(UserEntity userEntity, List<RoleDto> roles) {
        userEntity.getUserRoleLinks().clear();
        List<UserRoleLinkEntity> userRoleLinkEntities = roles.stream()
                .map(RoleDto::getId)
                .map(this::getEntityById)
                .map(roleEntity -> UserRoleLinkEntity.builder()
                        .user(userEntity)
                        .role(roleEntity)
                        .build())
                .map(wildcard -> (UserRoleLinkEntity) wildcard)
                .toList();
        userEntity.getUserRoleLinks().addAll(userRoleLinkEntities);
        userRoleLinkRepository.saveAllAndFlush(userRoleLinkEntities);
    }
}
