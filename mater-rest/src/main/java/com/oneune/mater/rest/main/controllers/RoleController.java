package com.oneune.mater.rest.main.controllers;

import com.oneune.mater.rest.main.services.RoleService;
import com.oneune.mater.rest.main.store.dtos.RoleDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("roles")
@CrossOrigin("*")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RoleController {

    RoleService roleService;

    @GetMapping
    public List<RoleDto> getRoles() {
        return roleService.getRoles();
    }
}
