package com.boot.learningspirit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Project: learningSpirit
 * @Author: DengYinzhe
 * @Date: 2023/3/28 21:27
 * @FileName: delMsgDto
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DelMsgDto {

    private Boolean delAll;

    private List<Long> msgIdList;
}
