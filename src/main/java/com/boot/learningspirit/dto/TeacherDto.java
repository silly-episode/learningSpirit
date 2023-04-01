package com.boot.learningspirit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Project: learningSpirit
 * @Author: DengYinzhe
 * @Date: 2023/4/1 11:27
 * @FileName: teacherDto
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDto {

    private String openId;

    private String userName;

}
