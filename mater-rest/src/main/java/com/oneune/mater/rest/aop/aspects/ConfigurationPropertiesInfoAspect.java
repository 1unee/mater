package com.oneune.mater.rest.aop.aspects;

import com.oneune.mater.rest.BeanUtils;
import com.oneune.mater.rest.aop.annotations.ConfigurationPropertiesInfo;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ConfigurationPropertiesInfoAspect implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object configurationPropertiesBean, @NonNull String beanName) throws BeansException {
        Class<?> beanClass = configurationPropertiesBean.getClass();
        if (beanClass.isAnnotationPresent(ConfigurationProperties.class)
                && beanClass.isAnnotationPresent(ConfigurationPropertiesInfo.class)) {
            BeanUtils.informAboutCreatingByProperties(configurationPropertiesBean);
        }
        return configurationPropertiesBean;
    }
}
