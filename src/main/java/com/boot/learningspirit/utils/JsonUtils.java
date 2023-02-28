package com.boot.learningspirit.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/2/3 11:29
 * @FileName: JsonUtils
 * @Description: 序列化反序列化
 */
public class JsonUtils {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 将对象转换成JSON数据
     *
     * @param data 对象
     * @return JSON数据
     */
    public static String getBeanToJson(Object data) {
        try {
            MAPPER.registerModule(new JavaTimeModule());
            return MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将JSON数据转换成对象
     *
     * @param jsonData JSON数据
     * @param beanType 对象类型
     * @return 对象
     */
    public static <T> T getJsonToBean(String jsonData, Class<T> beanType) {
        try {
            MAPPER.registerModule(new JavaTimeModule());
            return MAPPER.readValue(jsonData, beanType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将JSON数据转换成列表
     *
     * @param jsonData JSON数据
     * @param beanType 对象类型
     * @return 列表
     */
    public static <T> List<T> getJsonToList(String jsonData, Class<T> beanType) {
        try {
            MAPPER.registerModule(new JavaTimeModule());
            JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
            return MAPPER.readValue(jsonData, javaType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将JSON数据转换成Set集合
     *
     * @param jsonData    JSON数据
     * @param elementType 元素类型
     * @return Set集合
     */
    public static <E> Set<E> getJsonToSet(String jsonData, Class<E> elementType) {
        try {
            MAPPER.registerModule(new JavaTimeModule());
            JavaType javaType = MAPPER.getTypeFactory().constructCollectionType(Set.class, elementType);
            return MAPPER.readValue(jsonData, javaType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将JSON数据转换成Map集合
     *
     * @param jsonData  JSON数据
     * @param keyType   键类型
     * @param valueType 值类型
     * @return Map集合
     */
    public static <K, V> Map<K, V> getJsonToMap(String jsonData, Class<K> keyType, Class<V> valueType) {
        try {
            MAPPER.registerModule(new JavaTimeModule());
            JavaType javaType = MAPPER.getTypeFactory().constructMapType(Map.class, keyType, valueType);
            return MAPPER.readValue(jsonData, javaType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
