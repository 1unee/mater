package com.oneune.mater.rest.main.services;

import com.oneune.mater.rest.main.repositories.ActionRepository;
import com.oneune.mater.rest.main.store.dtos.UserDto;
import com.oneune.mater.rest.main.store.entities.ActionEntity;
import com.oneune.mater.rest.main.store.enums.ActionTypeEnum;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// todo: implement interfaces
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class ActionService {

    ActionRepository actionRepository;

    @Transactional
    public void track(UserDto user, ActionTypeEnum actionType, String value) {
        ActionEntity actionEntity = ActionEntity.builder()
                .userId(user.getId())
                .type(actionType)
                .body(value)
                .build();
        actionRepository.save(actionEntity);
    }
}
