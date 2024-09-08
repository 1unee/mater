package com.oneune.mater.rest.main.controllers;

import com.oneune.mater.rest.main.repositories.LogRepository;
import com.oneune.mater.rest.main.store.entities.LogEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("logs")
@CrossOrigin("*")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class LogController {

    LogRepository logRepository;

    @PostMapping
    @Transactional
    public void post(@RequestBody String stringifiedError) {
        logRepository.save(LogEntity.builder()
                .body(stringifiedError)
                .threwAt(Instant.now())
                .build());
    }
}
