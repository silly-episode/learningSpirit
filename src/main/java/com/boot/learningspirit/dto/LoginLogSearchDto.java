package com.boot.learningspirit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/3/27 9:17
 * @FileName: LoginLogSearch
 * @Description: 登录日志查询的Dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginLogSearchDto {


    private Integer pageNum;

    private Integer pageSize;
    /*开始时间*/
    private LocalDateTime beginTime;
    /*结束时间*/
    private LocalDateTime endTime;
    /*用户id、用户名、电话、邮箱*/
    private String accountOrTelOrNickNameOrUserId;
    //结果的描述
    private String logRemark;
    //角色
    private String role;
}
