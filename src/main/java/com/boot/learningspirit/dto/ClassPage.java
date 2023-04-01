package com.boot.learningspirit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Project: learningSpirit
 * @Author: DengYinzhe
 * @Date: 2023/4/1 11:40
 * @FileName: ClassPage
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassPage {

    /**
     * 页号
     */
    private Integer pageNum;

    /**
     * 页大小
     */
    private Integer pageSize;

    private String queryName;

}
