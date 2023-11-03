package com.example.demo.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Field;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class Utils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String convertArgsToJson(Object[] args) {
        try {
            return objectMapper.writeValueAsString(args);
        } catch (JsonProcessingException e) {
            log.error("JSON processing error", e);
            return null;
        }
    }

    public static String convertObjectToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("JSON processing error", e);
            return null;
        }
    }

    public static Field getField(Class<?> clazz, String fieldName) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            String alternateFieldName = fieldName.contains("_") ? toCamelCase(fieldName) : toSnakeCase(fieldName);
            try {
                field = clazz.getDeclaredField(alternateFieldName);
            } catch (NoSuchFieldException ex) {
                log.error("No such field: {}", fieldName);
            }
        }
        return field;
    }

    public static String toCamelCase(String s) {
        StringBuilder camelCaseString = new StringBuilder();
        boolean nextUpperCase = false;
        for (char c : s.toCharArray()) {
            if (c == '_') {
                nextUpperCase = true;
            } else if (nextUpperCase) {
                camelCaseString.append(Character.toUpperCase(c));
                nextUpperCase = false;
            } else {
                camelCaseString.append(Character.toLowerCase(c));
            }
        }
        return camelCaseString.toString();
    }

    private static String toSnakeCase(String s) {
        StringBuilder snakeCaseString = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (Character.isUpperCase(c)) {
                snakeCaseString.append('_').append(Character.toLowerCase(c));
            } else {
                snakeCaseString.append(c);
            }
        }
        return snakeCaseString.toString();
    }

    public static String getFieldValue(Object[] args, String fieldName){
        for (Object arg : args) {
            Field field = Utils.getField(arg.getClass(), fieldName);
            if (field != null) {
                field.setAccessible(true);
                try {
                    log.info("Field {}: {}", fieldName, field.get(arg));
                    return (String) field.get(arg);
                } catch (IllegalAccessException e) {
                    log.error("Error accessing field: {}", fieldName, e);
                    return null;
                }
            }
        }
        return null;
    }

}
