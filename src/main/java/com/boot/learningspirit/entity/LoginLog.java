package com.boot.learningspirit.entity;


import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * (LoginLog)表实体类
 *
 * @author makejava
 * @since 2023-04-01 17:07:34
 */
@SuppressWarnings("all")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginLog extends Model<LoginLog> {

    private Long logId;

    private LocalDateTime loginTime;
    //登录账户
    private String openId;
    //用户名
    private String nickName;
    //结果的描述
    private String logRemark;
    //角色
    private String role;


    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    public Serializable pkVal() {
        return this.logId;
    }
}

