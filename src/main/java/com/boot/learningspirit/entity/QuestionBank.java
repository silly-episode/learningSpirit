package com.boot.learningspirit.entity;


import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 题库(QuestionBank)表实体类
 *
 * @author makejava
 * @since 2023-03-16 10:37:38
 */
@SuppressWarnings("all")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class QuestionBank extends Model<QuestionBank> {
    //题目id
    @TableId(type = IdType.ASSIGN_ID)
    private Long questionId;
    //科目类型
    @ExcelProperty("subjectKind")
    private String subjectKind;
    //题目类型，dan、duo、pan
    @ExcelProperty("questionKind")
    private String questionKind;
    //题目内容
    @ExcelProperty("content")
    private String content;
    //选项 A:主题 B:数学 C:括号
    @ExcelProperty("choice")
    private String choice;
    //答案
    @ExcelProperty("answer")
    private String answer;
    //创建时间
    private LocalDateTime questionCreateTime;
    //排序id
    @ExcelProperty("orderId")
    private String orderId;


    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    public Serializable pkVal() {
        return this.questionId;
    }
}

