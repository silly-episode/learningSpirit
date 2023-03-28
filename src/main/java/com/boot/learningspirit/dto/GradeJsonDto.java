package com.boot.learningspirit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Project: learningSpirit
 * @Author: DengYinzhe
 * @Date: 2023/3/28 17:46
 * @FileName: gradeJsonDto
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GradeJsonDto {

    private String jsonStr;

    private int grade;
}
