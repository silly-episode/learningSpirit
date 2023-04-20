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
 * (OcrRecord)表实体类
 *
 * @author makejava
 * @since 2023-04-20 14:28:39
 */
@SuppressWarnings("all")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class OcrRecord extends Model<OcrRecord> {
    @TableId(type = IdType.ASSIGN_ID)
    private Long recordId;
    //文件路径
    private String filePath;

    private String openId;
    //记录时间
    private LocalDateTime recordTime;
    //结果
    private String recordText;
    //ocr类别
    private String ocrType;


    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    public Serializable pkVal() {
        return this.recordId;
    }
}

