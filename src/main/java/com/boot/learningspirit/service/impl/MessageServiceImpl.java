package com.boot.learningspirit.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.MapUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.learningspirit.dao.MessageDao;
import com.boot.learningspirit.dao.MessageReceiveDao;
import com.boot.learningspirit.entity.Message;
import com.boot.learningspirit.entity.MessageReceive;
import com.boot.learningspirit.service.MessageService;
import com.boot.learningspirit.utils.JsonUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

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
    public <V> void importExcel(HttpServletResponse response, String fileName, Class<V> v, List<V> list) throws IOException {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String finalFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + finalFileName + ".xlsx");
            // 这里需要设置不关闭流
            System.out.println(v == v.getDeclaredConstructor().newInstance().getClass());
            EasyExcel.write(response.getOutputStream(), v).autoCloseStream(Boolean.FALSE).sheet()
                    .doWrite(list);
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = MapUtils.newHashMap();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JsonUtils.getBeanToJson(map));
        }
    }


    @Override
    @Transactional
    public Boolean messageSave(Message msg, List<MessageReceive> msgReceive) {
        if (msgReceive.size() > 0) {
            return msgDao.insert(msg) > 0 && msgReceiveDao.insertBatch(msgReceive) > 0;
        } else {
            return false;
        }
    }


    @Override
    @Scheduled(cron = "59 28 10 21 4 ?")
    public void test() {
        System.out.println(1);
        int count = 0;
        count++;
        if (count != 0) {
            System.out.println("++++++++++++++=123123+++++++++++++++++++++");
        }

    }
}

