package com.oneune.mater.rest.main.services;

import com.oneune.mater.rest.main.repositories.RoleRepository;
import com.oneune.mater.rest.main.store.entities.RoleEntity;
import com.oneune.mater.rest.main.store.enums.RoleEnum;
import com.oneune.mater.rest.main.store.exceptions.BusinessLogicException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class RoleService {

    ModelMapper modelMapper;
    RoleRepository roleRepository;

    public RoleEntity get(RoleEnum roleConst) {
        return roleRepository.findByName(roleConst)
                .orElseThrow(() -> new BusinessLogicException("Not created role %s".formatted(roleConst.name())));
    }

    public RoleEnum getConst(String roleName) {
        return Arrays.stream(RoleEnum.values())
                .filter(roleConstant -> roleConstant.getRus().equalsIgnoreCase(roleName))
                .findFirst()
                .orElseThrow(() -> new BusinessLogicException("Not found role const like <%s>".formatted(roleName)));
    }

    public RoleEntity get(String roleName) {
        RoleEnum roleConst = getConst(roleName);
        return get(roleConst);
    }
}
