package com.oneune.mater.rest.common.aop.aspects;

import com.oneune.mater.rest.main.utils.BeanUtils;
import com.oneune.mater.rest.common.aop.annotations.ConfigurationBeansInfo;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ConfigurationBeansInfoAspect implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, @NonNull String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        boolean isConfigurationClass = beanClass.isAnnotationPresent(Configuration.class)
                || beanClass.isAnnotationPresent(AutoConfiguration.class);
        if (isConfigurationClass && beanClass.isAnnotationPresent(ConfigurationBeansInfo.class)) {
            BeanUtils.informAboutCreatingByConfig(beanClass);
        }
        return bean;
    }
}
