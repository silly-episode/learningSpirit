package com.boot.learningspirit.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.*;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.alibaba.excel.enums.poi.VerticalAlignmentEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Project: learningSpirit
 * @Author: DengYinzhe
 * @Date: 2023/4/20 15:39
 * @FileName: UserExcel
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("all")
//普通单元格的高度
@ContentRowHeight(26)
//头部单元格的高度
@HeadRowHeight(30)
//普通单元格的字体大小
@ContentFontStyle(fontHeightInPoints = 12)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER)
public class UserExcel {

    @NumberFormat(value = "#")
    @ColumnWidth(30)
    @ExcelProperty(value = "用户ID", index = 0)
    private String openId;
    //登录账户
    @ColumnWidth(23)
    @ExcelProperty(value = "用户名", index = 1)
    private String userName;
    @ColumnWidth(23)
    @ExcelProperty(value = "注册时间", index = 2)
    private LocalDateTime registerTime;
    @ColumnWidth(23)
    @ExcelProperty(value = "角色", index = 3)
    private String role;
    @ColumnWidth(23)
    @ExcelProperty(value = "上一次完成/发布任务时间", index = 4)
    private LocalDateTime statusTime;
    @ColumnWidth(23)
    @ExcelProperty(value = "完成/发布任务次数", index = 5)
    private int taskCount;


}
