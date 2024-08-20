package com.oneune.mater.rest.configs;

import com.oneune.mater.rest.aop.annotations.ConfigurationBeansInfo;
import com.oneune.mater.rest.configs.properties.TelegramBotProperties;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableConfigurationProperties(TelegramBotProperties.class)
@ConfigurationBeansInfo
@Log4j2
public class TelegramBotConfiguration {

    private CorsConfiguration getConfiguredCorsConfiguration() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(CorsConfiguration.ALL));
        configuration.setAllowedMethods(List.of(
                GET.name(), POST.name(), PUT.name(), DELETE.name(), OPTIONS.name()
        ));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of(CorsConfiguration.ALL));
        configuration.setExposedHeaders(List.of(CorsConfiguration.ALL));
        return configuration;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuredCorsConfiguration = this.getConfiguredCorsConfiguration();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuredCorsConfiguration);
        return source;
    }
}
