package com.boot.learningspirit.dto;

import com.boot.learningspirit.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Project: learningSpirit
 * @Author: DengYinzhe
 * @Date: 2023/3/28 20:15
 * @FileName: RmDto
 * @Description: 消息和消息接收的Dto
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RmDto extends Message {
    private Long msgReceiveId;
    //消息接收人
    private String receiveOpenId;
}
