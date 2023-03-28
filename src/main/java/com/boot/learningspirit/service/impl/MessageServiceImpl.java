package com.boot.learningspirit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.learningspirit.dao.MessageDao;
import com.boot.learningspirit.entity.Message;
import com.boot.learningspirit.service.MessageService;
import org.springframework.stereotype.Service;

/**
 * 消息表（）(Message)表服务实现类
 *
 * @author makejava
 * @since 2023-03-28 20:01:32
 */
@Service("messageService")
public class MessageServiceImpl extends ServiceImpl<MessageDao, Message> implements MessageService {

}

