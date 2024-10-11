package com.oneune.mater.rest.main.controllers;

import com.oneune.mater.rest.main.services.ActionService;
import com.oneune.mater.rest.main.store.dtos.UserDto;
import com.oneune.mater.rest.main.store.enums.ActionTypeEnum;
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
    public void track(@RequestBody UserDto user,
                      @RequestParam(name = "action-type") ActionTypeEnum actionType,
                      @RequestParam(name = "value") String value) {
        actionService.track(user, actionType, value);
    }

}
