package com.oneune.mater.rest.main.store.exceptions;

import com.oneune.mater.rest.main.repositories.ExceptionRepository;
import com.oneune.mater.rest.main.store.entities.ExceptionEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

@ControllerAdvice
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class GlobalExceptionHandler {

    ExceptionRepository exceptionRepository;

    @ExceptionHandler(Exception.class)
    @Transactional
    public void handleGlobalException(Exception e, WebRequest request) {
        ExceptionEntity exceptionEntity = buildExceptionEntity(e, request);
        exceptionRepository.save(exceptionEntity);
    }

    private ExceptionEntity buildExceptionEntity(Exception exception, WebRequest request) {
        return ExceptionEntity.builder()
                .exceptionType(exception.getClass().getName())
                .message(exception.getMessage())
                .rootCause(getRootCause(exception).getMessage())
                .className(exception.getStackTrace()[0].getClassName())
                .methodName(exception.getStackTrace()[0].getMethodName())
                .stackTrace(getStackTraceAsString(exception))
                .timestamp(LocalDateTime.now())
                .requestUrl(request.getDescription(false))
                .build();
    }

    private Throwable getRootCause(Throwable throwable) {
        Throwable cause = throwable.getCause();
        return Objects.isNull(cause.getCause()) ? throwable : getRootCause(cause);
    }

    private String getStackTraceAsString(Exception exception) {
        StringBuilder result = new StringBuilder();
        Arrays.stream(exception.getStackTrace())
                .forEach(stackTrace -> result.append(stackTrace.toString()).append(";"));
        return result.toString();
    }
}
