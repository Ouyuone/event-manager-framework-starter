package com.my.framework;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.framework.exception.JsonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class JsonUtil implements ApplicationContextAware {

    private static ObjectMapper objectMapper;

    public static String toJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            log.warn("write to json string failed: {}, exception: ", object, e);
            throw new JsonException(String.format("serialize failed, message is %s", object), e);
        }
    }

    public static String toPrettyJsonString(Object object) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (Exception e) {
            log.warn("write to json string failed: {}, exception: ", object, e);
            throw new JsonException(String.format("serialize failed, message is %s", object), e);
        }
    }


    public static <T> T convertToObject(String jsonString, Class<T> clazz) {
        if (StringUtils.isBlank(jsonString)) {
            return null;
        }
        try {
            return objectMapper.readValue(jsonString, clazz);
        } catch (Exception e) {
            log.warn("convert json string {} to {} failed. exception: ", jsonString, clazz.getName(), e);
            throw new JsonException(String.format("deserialize failed, message is %s", jsonString), e);
        }
    }

    public static <T> T convertToObject(String jsonString, TypeReference<T> type) {
        if (StringUtils.isBlank(jsonString)) {
            return null;
        }
        try {
            return objectMapper.readValue(jsonString, type);
        } catch (Exception e) {
            log.warn("convert json string {} to {} failed. exception:  ", jsonString, type.getType().getTypeName(), e);
            throw new JsonException(String.format("deserialize failed, message is %s", jsonString), e);
        }
    }

    public static <T> T convertToObject(Object content, Class<T> valueType) {
        if (ObjectUtils.isEmpty(content)) {
            return null;
        }
        return objectMapper.convertValue(content, valueType);
    }

    public static Map<String, Object> convertToMap(String content) {
        if (StringUtils.isBlank(content)) {
            return null;
        }
        if (StringUtils.startsWith(content, "{") && StringUtils.endsWith(content, "}")) {
            return convertToObject(content, HashMap.class);
        } else {
            return Map.of("content", content);
        }
    }

    public static <T> List<T> convertToListObject(String content, Class<T> valueType) {
        if (StringUtils.isBlank(content)) {
            return null;
        }
        try {
            return objectMapper.readValue(content, new TypeReference<List<T>>() {
                @Override
                public Type getType() {
                    return TypeUtils.parameterize(List.class, valueType);
                }
            });
        } catch (JsonProcessingException e) {
            log.warn("convert json string {} to List<{}> failed. exception:  ", content, valueType.getName(), e);
            throw new JsonException(String.format("deserialize failed, message is %s", content), e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        JsonUtil.objectMapper = applicationContext.getBean(ObjectMapper.class);
    }

}