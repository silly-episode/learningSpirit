package com.boot.learningspirit.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * (MessageReceive)表实体类
 *
 * @author makejava
 * @since 2023-03-28 20:02:10
 */
@SuppressWarnings("all")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MessageReceive extends Model<MessageReceive> {
    @TableId(type = IdType.AUTO)
    private Long msgReceiveId;
    //消息接收人
    private String receiveOpenId;
    //消息主体id
    private Long msgId;

    @TableField(exist = false)
    private List<Message> messages;

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    public Serializable pkVal() {
        return this.msgReceiveId;
    }
}

