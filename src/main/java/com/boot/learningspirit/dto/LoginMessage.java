package com.boot.learningspirit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/2/2 20:37
 * @FileName: LoginUser
 * @Description: 通用登录dto;account为账户、电话；password为密码或者短信验证码
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginMessage {

    private String userName;

    private String subject;

}
