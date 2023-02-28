package com.boot.learningspirit.utils;


import org.springframework.beans.BeanUtils;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/2/2 20:06
 * @FileName: BeanDtoVoUtils
 * @Description: DTO和Entity转换
 */
public class BeanDtoVoUtils {

    /**
     * TODO  dot ,Do ,entity 相互转换
     * 同：BeanUtils.copyProperties(dtoEntity, newInstance);
     *
     * @param oldClass 原数据--Dto，Vo，entity
     * @param newClass 转换为--Dto，Vo，entity
     */
    public static <E> E convert(Object oldClass, Class<E> newClass) {
        // 判断oldClass 是否为空!
        if (oldClass == null) {
            return null;
        }
        // 判断newClass 是否为空
        if (newClass == null) {
            return null;
        }
        try {
            // 创建新的对象实例
            E newInstance = newClass.getDeclaredConstructor().newInstance();

            // 把原对象数据拷贝到新的对象
            BeanUtils.copyProperties(oldClass, newInstance);
            // 返回新对象
            return newInstance;
        } catch (Exception e) {
            return null;
        }
    }


}
