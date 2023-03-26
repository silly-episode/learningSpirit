package com.boot.learningspirit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Project: learningSpirit
 * @Author: DengYinzhe
 * @Date: 2023/3/26 15:06
 * @FileName: ApplyClassMember
 * @Description:
 */
@SuppressWarnings("all")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplyClassMember {
    //主键
    @TableId(type = IdType.ASSIGN_ID)
    private Long applyId;
    //用户id
    private String openId;
    //班级id
    private Long classId;
    //申请加入时间
    private LocalDateTime applyTime;
    //身份类型
    private String type;
    //申请结果
    private String Result;
    //处理人id
    private String dealOpenId;
    //是否处理
    private Boolean deal;
    //申请班级名称
    private String className;


}
