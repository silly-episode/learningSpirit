package com.boot.learningspirit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.learningspirit.dao.OcrRecordDao;
import com.boot.learningspirit.entity.OcrRecord;
import com.boot.learningspirit.service.OcrRecordService;
import org.springframework.stereotype.Service;

/**
 * (OcrRecord)表服务实现类
 *
 * @author makejava
 * @since 2023-04-20 14:28:39
 */
@Service("ocrRecordService")
public class OcrRecordServiceImpl extends ServiceImpl<OcrRecordDao, OcrRecord> implements OcrRecordService {

}

