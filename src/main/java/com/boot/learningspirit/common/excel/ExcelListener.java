package com.boot.learningspirit.common.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.util.ListUtils;
import com.boot.learningspirit.entity.QuestionBank;
import com.boot.learningspirit.service.QuestionBankService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * @Project: hospitalDrugInformationManagement
 * @Author: 86178
 * @Date: 2023/2/23 15:15
 * @FileName: ExcelListener
 * @Description: ""
 */
@Slf4j
public class ExcelListener extends AnalysisEventListener<QuestionBank> {

    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;
    private final QuestionBankService questionBankService;
    /**
     * 缓存的数据
     */
    private List<QuestionBank> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    public ExcelListener(QuestionBankService questionBankService) {
        this.questionBankService = questionBankService;
    }


    /**
     * @param headMap:
     * @param context:
     * @Return: void
     * @Author: DengYinzhe
     * @Description: 读取表头中的内容
     * @Date: 2023/3/16 15:03
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头的内容为" + headMap);
    }

    /**
     * @param data:
     * @param context:
     * @Return: void
     * @Author: DengYinzhe
     * @Description: 这个每一条数据解析都会来调用
     * @Date: 2023/3/16 15:02
     */
    @Override
    public void invoke(QuestionBank data, AnalysisContext context) {
        cachedDataList.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (cachedDataList.size() >= BATCH_COUNT) {
            questionBankService.saveBatch(cachedDataList);
            // 存储完成清理 list
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    /**
     * @param context:
     * @Return: void
     * @Author: DengYinzhe
     * @Description: 所有数据解析完成了 都会来调用
     * @Date: 2023/3/16 15:03
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
//        //插入数据库
//        System.out.println(cachedDataList);

    }

    /**
     * @param exception:
     * @param context:
     * @Return: void
     * @Author: DengYinzhe
     * @Description: 监听器实现这个方法就可以在读取数据的时候获取到异常信息
     * @Date: 2023/3/16 15:03
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) {
        log.error("解析失败，但是继续解析下一行:{}", exception.getMessage());
        // 如果是某一个单元格的转换异常 能获取到具体行号
        // 如果要获取头的信息 配合invokeHeadMap使用
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;
            log.error("第{}行，第{}列解析异常", excelDataConvertException.getRowIndex(),
                    excelDataConvertException.getColumnIndex());
        }
    }

}
