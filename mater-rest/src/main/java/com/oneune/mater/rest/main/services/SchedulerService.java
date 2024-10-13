package com.oneune.mater.rest.main.services;

import com.oneune.mater.rest.common.aop.annotations.LogExecutionDuration;
import com.oneune.mater.rest.main.configs.properties.CronProperties;
import com.oneune.mater.rest.main.readers.FileReader;
import com.oneune.mater.rest.main.store.entities.core.AbstractFileEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class SchedulerService {

    SelectelS3Service selectelS3Service;
    FileReader fileReader;
    CronProperties cronProperties;

    @Async
    @Scheduled(cron = "${cron.config.clean-orphan-objects.expression}")
    @LogExecutionDuration(logStartMessage = true)
    public void performDailyTask() {
        List<String> existingFiles = fileReader.writeBaseQuery()
                .fetch()
                .stream()
                .map(AbstractFileEntity::getName)
                .toList();
        List<String> filesToRemove = selectelS3Service
                .getFilenames()
                .stream()
                .filter(filename -> !existingFiles.contains(filename))
                .toList();
        selectelS3Service.deleteObjects(filesToRemove);
    }
}
