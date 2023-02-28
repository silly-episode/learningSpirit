package com.boot.learningspirit.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/1/30 9:58
 * @FileName: CodeMsg
 * @Description: 自定义返回码
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeMsg {
    /**
     * @Author: DengYinzhe
     * @Description: 通用状态码
     * @Date: 2023/2/6 21:21
     */
    public static final CodeMsg SUCCESS = new CodeMsg(HttpStatus.OK.value(), "success");
    public static final CodeMsg BAD_REQUEST = new CodeMsg(HttpStatus.BAD_REQUEST.value(), "请求无效");
    public static final CodeMsg SERVER_ERROR = new CodeMsg(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务端异常");
    public static final CodeMsg NO_HANDLER_FOUND = new CodeMsg(HttpStatus.NOT_FOUND.value(), "未找到对应资源");
    public static final CodeMsg UNAUTHORIZED = new CodeMsg(HttpStatus.UNAUTHORIZED.value(), "未认证或登录状态过期");
    public static final CodeMsg FORBIDDEN = new CodeMsg(HttpStatus.FORBIDDEN.value(), "未授权");
    /**
     * @Author: DengYinzhe
     * @Description: 自定义状态码
     * @Date: 2023/2/6 21:21
     */
    public static final CodeMsg PARAMETER_ERROR = new CodeMsg(4000, "参数不正确！");
    /**
     * @Author: DengYinzhe
     * @Description: 用户相关：验证码
     * @Date: 2023/2/6 21:21
     */
    public static final CodeMsg CAPTCHA_EXPIRED = new CodeMsg(4001, "验证码不存在或已过期");
    public static final CodeMsg CAPTCHA_INVALID = new CodeMsg(4002, "验证码错误");
    /**
     * @Author: DengYinzhe
     * @Description: 用户相关：认证授权
     * @Date: 2023/2/6 21:21
     */
    public static final CodeMsg BAD_CREDENTIAL = new CodeMsg(4003, "用户名或密码错误");
    public static final CodeMsg ACCOUNT_NOT_FOUND = new CodeMsg(4004, "账号不存在");
    public static final CodeMsg ACCOUNT_NOT_ACTIVATED = new CodeMsg(4005, "账号未激活");
    public static final CodeMsg SHIRO_ERROR = new CodeMsg(4006, "服务器Shiro运行错误");
    /**
     * @Author: DengYinzhe
     * @Description: 角色相关
     * @Date: 2023/2/6 21:23
     */
    public static final CodeMsg ROLE_ID_CONFLICT = new CodeMsg(4101, "角色ID已存在");
    public static final CodeMsg ROLE_USER_CONNECTED = new CodeMsg(4102, "角色用户存在关联");
    public static final CodeMsg ROLE_PERMISSION_DENIED = new CodeMsg(4103, "角色权限不足");
    private int code;
    private String msg;


}
