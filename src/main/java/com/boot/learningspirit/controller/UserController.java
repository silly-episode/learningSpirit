package com.boot.learningspirit.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.learningspirit.common.result.Result;
import com.boot.learningspirit.dto.ClassPage;
import com.boot.learningspirit.dto.LoginLogSearchDto;
import com.boot.learningspirit.entity.*;
import com.boot.learningspirit.service.*;
import com.boot.learningspirit.utils.EncryptUtil;
import com.boot.learningspirit.utils.GetUserInfoUtil;
import com.boot.learningspirit.utils.JwtUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Project: learningSpirit
 * @Author: DengYinzhe
 * @Date: 2023/2/20 10:34
 * @FileName: UserController
 * @Description:
 */
@RestController
@RequestMapping("user")
//@SuppressWarnings("all")
public class UserController {

    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    private UserService userService;
    @Resource
    private MemberTaskStatusService memberTaskStatusService;
    @Resource
    private ClassMemberService classMemberService;
    @Resource
    private ClassService classService;
    @Resource
    private LoginLogService loginLogService;

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
//            System.out.println(openid);
//            System.out.println("code:" + code);
//            System.out.println("role:" + role);
//            System.out.println("userName:" + userName);
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
            if (user != null && user.getUserStatus() == 1) {
                return Result.error(5051, "该用户已经锁定");
            }

            if (user != null) {
                result.put("userInfo", user);
                return Result.success(result); //用户存在，返回结果
            } else if (userName != null && role != null) { //用户不存在，且用户名不为空
                user = new User();
                user.setOpenId(openid);
                user.setRole(role);
                user.setUserName(userName);
                user.setRegisterTime(LocalDateTime.now());
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


    /**
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 获取类型用户
     * @Date: 2023/4/1 10:53
     */
    @PostMapping("getTypeUser")
    public Result getTypeUser(@RequestBody Map<String, String> map) {
        String userType = map.get("userType");
        if (userType == null) {
            return Result.error("参数错误");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role", userType);
        List<User> userList = userService.list(queryWrapper);
        return Result.success(userList);
    }

    /**
     * @param user:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 更新用户信息
     * @Date: 2023/4/1 15:46
     */
    @PostMapping("updateUser")
    public Result updateUser(@RequestBody User user) {
        if (userService.updateById(user)) {
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }


    /**
     * @param userSearch:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 分页搜索用户
     * @Date: 2023/4/1 16:40
     */
    @PostMapping("userSearch")
    public Result userSearch(@RequestBody ClassPage userSearch) {
        System.out.println("123");
        String oftenParam = userSearch.getQueryName();
        Page<User> pageInfo = new Page<>(userSearch.getPageNum(), userSearch.getPageSize());
        QueryWrapper<MemberTaskStatus> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        QueryWrapper<ClassMember> classMemberQueryWrapper = new QueryWrapper<>();
        wrapper
                .and(!"".equals(oftenParam),
                        e -> e.like(User::getUserName, oftenParam)
                                .or().eq(User::getOpenId, oftenParam)
                );
        userService.page(pageInfo, wrapper);
        for (User record : pageInfo.getRecords()) {
            record.setBanJiList(new ArrayList<>(100));
//            上次完成/发布的任务时间
            queryWrapper.clear();
            queryWrapper
                    .eq("open_id", record.getOpenId())
                    .orderByDesc("status_time");
            if ("student".equals(record.getRole())) {
                queryWrapper.eq("status", "已完成");
            }
            List<MemberTaskStatus> list = new ArrayList<>();
            list = memberTaskStatusService.list(queryWrapper);
            if (list.size() > 0) {
                record.setStatusTime(list.get(0).getStatusTime());
            }
//          已经完成或发布任务的数量
            int taskCount = list.size();
//          所在班级的信息
            classMemberQueryWrapper.clear();
            classMemberQueryWrapper.eq("open_id", record.getOpenId());
            List<ClassMember> classMemberList = classMemberService.list(classMemberQueryWrapper);
            for (ClassMember member : classMemberList) {
                BanJi banJi = classService.getById(member.getClassId());
                record.getBanJiList().add(banJi);
            }

        }
        return Result.success(pageInfo);
    }

    /**
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 删除用户
     * @Date: 2023/4/1 16:57
     */
    @PostMapping("deleteUser")
    public Result deleteUser(@RequestBody Map<String, String> map) {
        String openId = map.get("id");
        String type = map.get("type");
        if (openId == null || type == null) {
            return Result.error("参数错误");
        }
        User user = userService.getById(openId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();

        updateWrapper.eq("open_id", openId);

        if ("lock".equals(type)) {
            updateWrapper.set("user_status", 1);
        } else {
            updateWrapper.set("user_status", 0);
        }

        if (userService.update(updateWrapper)) {
            return Result.success();
        } else {
            return Result.error("失败");
        }

    }


    /**
     * @param logSearch:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 登录日志分页查询和日志导出
     * @Date: 2023/3/27 10:03
     */
    @PostMapping("actionLog")
    public Result<Page<LoginLog>> commonUserLog(@RequestBody LoginLogSearchDto logSearch) throws IOException {
        /*查询信息*/
        Page<LoginLog> pageInfo = new Page<>(logSearch.getPageNum(), logSearch.getPageSize());
        LambdaQueryWrapper<LoginLog> wrapper = new LambdaQueryWrapper<>();
        String oftenParam = logSearch.getAccountOrTelOrNickNameOrUserId();
        String userStatus;
        wrapper
                .ge(null != logSearch.getBeginTime(), LoginLog::getLoginTime, logSearch.getBeginTime())
                .le(null != logSearch.getEndTime(), LoginLog::getLoginTime, logSearch.getEndTime())
                .and(null != oftenParam,
                        e -> e.like(LoginLog::getNickName, oftenParam)
                                .or().eq(LoginLog::getLogRemark, oftenParam)
                                .or().eq(LoginLog::getOpenId, oftenParam)
                )
                .orderByDesc(LoginLog::getLoginTime);
        loginLogService.page(pageInfo, wrapper);
        return Result.success(pageInfo);

    }


}
