package com.boot.learningspirit.controller;

import com.boot.learningspirit.common.result.Result;
import com.boot.learningspirit.entity.User;
import com.boot.learningspirit.service.UserService;
import com.boot.learningspirit.utils.EncryptUtil;
import com.boot.learningspirit.utils.GetUserInfoUtil;
import com.boot.learningspirit.utils.JwtUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Project: learningSpirit
 * @Author: DengYinzhe
 * @Date: 2023/2/20 10:34
 * @FileName: WxLoginController
 * @Description:
 */
@RestController
@RequestMapping("user")
@SuppressWarnings("all")
public class WxLoginController {

    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    private UserService userService;

    /**
     * @param code:
     * @param role:
     * @param userName:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 登录注册
     * @Date: 2023/2/22 9:13
     */
    @GetMapping("/index/login")
    public Result authorizeLogin(
            @RequestParam("code") String code,
            @RequestParam(required = false, value = "role") String role,
            @RequestParam(required = false, value = "userName") String userName) {

        //通过code换取信息
        JSONObject resultJson = GetUserInfoUtil.getResultJson(code);

        if (resultJson.has("openid")) {
            //获取sessionKey和openId
            String sessionKey = resultJson.get("session_key").toString();
            String openid = resultJson.get("openid").toString();
            System.out.println(openid);
            System.out.println("code:" + code);
            System.out.println("role:" + role);
            System.out.println("userName:" + userName);
            //生成自定义登录态session
            String session = null;
            Map<String, String> sessionMap = new HashMap<>();

            sessionMap.put("sessionKey", sessionKey);
            sessionMap.put("openid", openid);
            session = JSONObject.fromObject(sessionMap).toString();

            //加密session
            try {
                EncryptUtil encryptUtil = new EncryptUtil();
                session = encryptUtil.encrypt(session);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //生成token
            String token = jwtUtil.getToken(session);
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            System.out.println(token);
            //查询用户是否存在
            User user = userService.getById(openid);
            if (user != null) {
                result.put("userInfo", user);
                return Result.success(result); //用户存在，返回结果
            } else if (userName != null && role != null) { //用户不存在，且用户名不为空
                user = new User();
                user.setOpenId(openid);
                user.setRole(role);
                user.setUserName(userName);
                boolean rs = userService.save(user);
                if (!rs) {
                    return Result.error("登录失败");
                }
                result.put("userInfo", user);
                return Result.success(result);
            }
        }
        return Result.error("授权失败");
    }

    /**
     * @param user:
     * @param request:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 更新用户信息
     * @Date: 2023/2/22 9:14
     */
    @PostMapping("/index/updateUser")
    public Result insertPersonInfo(@RequestBody User user,
                                   HttpServletRequest request) {

        //获取请求头token
        String token = request.getHeader("Authorization");
        //从token中获取openid
        String openid = jwtUtil.getOpenidFromToken(token);
        user.setOpenId(openid);
        System.out.println(user.toString());
        boolean result = userService.updateById(user);
        if (result) {
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }


    /**
     * @param openId:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 获取用户信息通过openId
     * @Date: 2023/2/22 9:17
     */
    @GetMapping("index/getUser")
    public Result getUser(@RequestParam("openId") String openId) {
        System.out.println(openId);
        User user = userService.getById(openId);
        if (user == null) {
            return Result.error("用户不存在");
        } else {
            return Result.success(user);
        }
    }

}
