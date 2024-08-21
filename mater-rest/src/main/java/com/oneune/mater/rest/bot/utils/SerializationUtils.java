package com.oneune.mater.rest.bot.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

@UtilityClass
@Log4j2
public final class SerializationUtils {

    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public boolean isObject(String body) {
        try {
            return OBJECT_MAPPER.readTree(body).isObject();
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }

    public <T> String toJson(T toSerialize) {
        try {
            return OBJECT_MAPPER.writeValueAsString(toSerialize);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing object to JSON", e);
        }
    }

    public <T> T fromJson(String toDeserialize, Class<T> tClass) {
        try {
            return OBJECT_MAPPER.readValue(toDeserialize, tClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing object from JSON", e);
        }
    }
}
