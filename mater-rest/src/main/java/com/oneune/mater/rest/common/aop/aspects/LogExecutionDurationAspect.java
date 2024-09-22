package com.oneune.mater.rest.common.aop.aspects;

import com.oneune.mater.rest.common.aop.annotations.LogExecutionDuration;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Log4j2
public class LogExecutionDurationAspect {

    private Method logStartMethodIfNeeded(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        LogExecutionDuration logExecutionDurationAnnotation = method.getAnnotation(LogExecutionDuration.class);
        if (logExecutionDurationAnnotation.logStartMessage()) {
            log.info("Method '{}' started", method.getName());
        }
        return method;
    }

    @Around("@annotation(com.oneune.mater.rest.common.aop.annotations.LogExecutionDuration)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = logStartMethodIfNeeded(joinPoint);
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long finish = System.currentTimeMillis();
        log.info("Method '{}' executed in {} ms", method.getName(), finish - start);
        return proceed;
    }
}
