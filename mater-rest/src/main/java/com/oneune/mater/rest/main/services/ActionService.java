package com.oneune.mater.rest.main.services;

import com.oneune.mater.rest.main.repositories.ActionRepository;
import com.oneune.mater.rest.main.store.dtos.UserDto;
import com.oneune.mater.rest.main.store.entities.ActionEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

// todo: implement interfaces
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class ActionService {

    ActionRepository actionRepository;

    @Transactional
    public void track(UserDto user, String route) {
        ActionEntity actionEntity = ActionEntity.builder()
                .userId(user.getId())
                .type("REDIRECT")
                .body(route)
                .timestamp(Instant.now())
                .build();
        actionRepository.save(actionEntity);
    }
}
