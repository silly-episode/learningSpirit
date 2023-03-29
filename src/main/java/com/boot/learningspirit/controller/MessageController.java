package com.boot.learningspirit.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.learningspirit.common.result.Result;
import com.boot.learningspirit.dto.DelMsgDto;
import com.boot.learningspirit.dto.RmDto;
import com.boot.learningspirit.entity.MessageReceive;
import com.boot.learningspirit.service.MessageReceiveService;
import com.boot.learningspirit.service.MessageService;
import com.boot.learningspirit.utils.BeanDtoVoUtils;
import com.boot.learningspirit.utils.JwtUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 消息表（）(Message)表控制层
 *
 * @author makejava
 * @since 2023-03-28 20:01:32
 */
@RestController
@RequestMapping("message")
public class MessageController {
    @Resource
    JwtUtil jwtUtil;
    /**
     * 服务对象
     */
    @Resource
    private MessageService messageService;
    @Resource
    private MessageReceiveService receiveService;


    /**
     * @param pageSize:
     * @param pageNum:
     * @param request:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 获取消息列表
     * @Date: 2023/3/28 21:10
     */
    @GetMapping("getMessageList")
    public Result getMessageList(@RequestParam Integer pageSize,
                                 @RequestParam Integer pageNum,
                                 HttpServletRequest request) {
        //获取请求头token
        String token = request.getHeader("Authorization");
        //从token中获取openid
        String openid = jwtUtil.getOpenidFromToken(token);
//        获取所有的receive的消息
        Page<MessageReceive> pageInfo = new Page<>(pageNum, pageSize);
        QueryWrapper<MessageReceive> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("receive_open_id", openid);
        receiveService.page(pageInfo, queryWrapper);
//        获取每个消息体
        List<RmDto> list = new ArrayList<>();
        for (MessageReceive record : pageInfo.getRecords()) {
            RmDto temp = BeanDtoVoUtils.convert(messageService.getById(record.getMsgId()), RmDto.class);
            temp.setReceiveOpenId(record.getReceiveOpenId());
            temp.setMsgReceiveId(record.getMsgReceiveId());
            temp.setDeal(record.getDeal());
            list.add(temp);
        }
//        重新生成page和排序
        Page<RmDto> finalPageInfo = new Page<>(pageNum, pageSize);
        list.sort(Comparator.comparing(RmDto::getMessageCreateTime).reversed());
        finalPageInfo.setRecords(list);
        finalPageInfo.setTotal(pageInfo.getTotal());
        return Result.success(finalPageInfo);
    }

    /**
     * @param delMsgDto:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 删除消息
     * @Date: 2023/3/28 21:30
     */
    @PostMapping("deleteMessage")
    public Result deleteMessage(@RequestParam DelMsgDto delMsgDto, HttpServletRequest request) {
        //获取请求头token
        String token = request.getHeader("Authorization");
        //从token中获取openid
        String openid = jwtUtil.getOpenidFromToken(token);
//        分情况删消息接收
        QueryWrapper<MessageReceive> queryWrapper = new QueryWrapper<>();
        if (delMsgDto.getDelAll()) {
            queryWrapper.eq("receive_open_id", openid);
        } else {
            queryWrapper
                    .eq("receive_open_id", openid)
                    .in("msg_id", delMsgDto.getMsgIdList());
        }
        receiveService.remove(queryWrapper);
//            如果消息表对应的receive的表的count为空则删除消息表中的消息
        queryWrapper.clear();
        queryWrapper.in("msg_id", delMsgDto.getMsgIdList());
        List<MessageReceive> list = receiveService.list(queryWrapper);
        for (MessageReceive receive : list) {
            queryWrapper.clear();
            queryWrapper.eq("msg_id", receive.getMsgId());
            if (0 == receiveService.count(queryWrapper)) {
                messageService.removeById(receive.getMsgId());
            }
        }
        return Result.success("删除成功");
    }

}

