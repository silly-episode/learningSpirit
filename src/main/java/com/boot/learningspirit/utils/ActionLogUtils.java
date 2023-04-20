package com.boot.learningspirit.utils;


import com.boot.learningspirit.entity.ActionLog;
import com.boot.learningspirit.entity.User;
import com.boot.learningspirit.service.ActionLogService;
import com.boot.learningspirit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/4/13 18:14
 * @FileName: ActionLogUtils
 * @Description: 录入操作日志
 */
@Component
@Slf4j
public class ActionLogUtils {

    public final String INSERT = "INSERT";
    public final String INSERT_BATCH = "INSERT_BATCH";
    public final String DELETE = "DELETE";
    public final String DELETE_BATCH = "DELETE_BATCH";
    public final String UPDATE = "UPDATE";
    public final String EXPORT = "EXPORT";
    @Resource
    private ActionLogService actionLogService;
    @Resource
    private JwtUtil jwtUtils;
    @Resource
    private UserService adminService;

    public void saveActionLog(HttpServletRequest request, String actionKind, String remark) {
        String adminId = jwtUtils.getUserIdFromRequest(request);
        User admin = adminService.getById(adminId);
        if (admin == null) {
            return;
        }
        ActionLog log = new ActionLog()
                .setOpenId(adminId)
                .setActionTime(LocalDateTime.now())
                .setActionKind(actionKind)
                .setRemark(remark)
                .setUserName(admin.getUserName())
                .setRole(admin.getRole());
        actionLogService.save(log);
    }

}
