package com.boot.learningspirit.utils;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * @param oldList:
     * @param v:
     * @Return: List<V>
     * @Author: DengYinzhe
     * @Description: list转换
     * @Date: 2023/3/27 13:04
     */
    public static <T, V> List<V> convertList(List<T> oldList, Class<V> v) {
        try {
            List<V> newList = new ArrayList<>();
            for (T t : oldList) {
                V temp = (V) BeanDtoVoUtils.convert(t, v.getDeclaredConstructor().newInstance().getClass());
                newList.add(temp);
            }
            return newList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param v:
     * @Return: Page<V>
     * @Author: DengYinzhe
     * @Description: Page<Entity> 分页对象转 Page<Vo>  ( list 循环)
     * @Date: 2023/2/2 20:33
     */
    public static <T, V> Page<V> pageVo(Page<T> oldPage, Class<V> v) {
        try {
            List<T> tList = oldPage.getRecords();
            List<V> voList = new ArrayList<>();
            for (T t : tList) {
                V temp = (V) BeanDtoVoUtils.convert(t, v.getDeclaredConstructor().newInstance().getClass());
                voList.add(temp);
            }
            Page<V> newPage = new Page<V>(
                    oldPage.getCurrent(),
                    oldPage.getSize(),
                    oldPage.getTotal(),
                    oldPage.searchCount());
            newPage.setCountId(oldPage.getCountId());
            newPage.setRecords(voList);
            newPage.setMaxLimit(oldPage.getMaxLimit());
            newPage.setPages(oldPage.getPages());
            newPage.setOptimizeCountSql(oldPage.optimizeCountSql());
            newPage.setOrders(oldPage.getOrders());
            newPage.setOptimizeJoinOfCountSql(oldPage.optimizeJoinOfCountSql());
            return newPage;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
