package com.boot.learningspirit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.learningspirit.dao.MessageReceiveDao;
import com.boot.learningspirit.entity.MessageReceive;
import com.boot.learningspirit.service.MessageReceiveService;
import org.springframework.stereotype.Service;

/**
 * (MessageReceive)表服务实现类
 *
 * @author makejava
 * @since 2023-03-28 20:02:10
 */
@Service("messageReceiveService")
public class MessageReceiveServiceImpl extends ServiceImpl<MessageReceiveDao, MessageReceive> implements MessageReceiveService {

}

