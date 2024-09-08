package com.oneune.mater.rest.main.controllers;

import com.oneune.mater.rest.main.services.ActionService;
import com.oneune.mater.rest.main.store.dtos.UserDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("actions")
@CrossOrigin("*")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ActionController {

    ActionService actionService;

    @PostMapping
    public void track(@RequestBody UserDto user, @RequestParam String route) {
        actionService.track(user, route);
    }

}
