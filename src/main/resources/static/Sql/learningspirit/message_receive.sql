create table if not exists message_receive
(
    msg_receive_id  bigint auto_increment
        primary key,
    receive_open_id varchar(300)         null comment '消息接收人',
    msg_id          bigint               null comment '消息主体id',
    deal            tinyint(1) default 0 null comment '是否处理或已读'
);

