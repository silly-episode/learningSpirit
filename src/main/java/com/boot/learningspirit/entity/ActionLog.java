package com.boot.learningspirit.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * (ActionLog)表实体类
 *
 * @author makejava
 * @since 2023-04-20 10:50:37
 */
@SuppressWarnings("all")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ActionLog extends Model<ActionLog> {
    @TableId(type = IdType.ASSIGN_ID)
    private Long actionId;
    //操作时间
    private LocalDateTime actionTime;
    //持有人ID
    private String openId;
    //持有人姓名
    private String userName;
    //角色
    private String role;
    //操作类型
    private String actionKind;
    //结果描述
    private String remark;


    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    public Serializable pkVal() {
        return this.actionId;
    }
}

