package com.boot.learningspirit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boot.learningspirit.entity.Message;
import com.boot.learningspirit.entity.MessageReceive;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 消息表（）(Message)表服务接口
 *
 * @author makejava
 * @since 2023-03-28 20:01:32
 */
public interface MessageService extends IService<Message> {


    Boolean messageSave(Message msg, List<MessageReceive> msgReceive);

    <V> void importExcel(HttpServletResponse response, String fileName, Class<V> v, List<V> list) throws IOException;

}

