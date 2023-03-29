package com.boot.learningspirit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.learningspirit.dao.MessageDao;
import com.boot.learningspirit.dao.MessageReceiveDao;
import com.boot.learningspirit.entity.Message;
import com.boot.learningspirit.entity.MessageReceive;
import com.boot.learningspirit.service.MessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 消息表（）(Message)表服务实现类
 *
 * @author makejava
 * @since 2023-03-28 20:01:32
 */
@Service("messageService")
public class MessageServiceImpl extends ServiceImpl<MessageDao, Message> implements MessageService {

    @Resource
    private MessageDao msgDao;

    @Resource
    private MessageReceiveDao msgReceiveDao;


    @Override
    @Transactional
    public Boolean messageSave(Message msg, List<MessageReceive> msgReceive) {
        if (msgReceive.size() > 0) {
            return msgDao.insert(msg) > 0 && msgReceiveDao.insertBatch(msgReceive) > 0;
        } else {
            return false;
        }
    }
}

