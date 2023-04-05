create table if not exists message
(
    msg_id              bigint        not null comment '消息ID'
        primary key,
    msg_type            int           null comment '消息类别',
    msg_title           varchar(200)  null comment '消息标题',
    msg_content         varchar(1000) null comment '内容',
    open_id             varchar(300)  null,
    class_id            bigint        null,
    message_create_time datetime      null comment '消息产生时间',
    type                varchar(20)   null comment '角色类型',
    task_id             bigint        null,
    task_type           varchar(200)  null comment '任务类型',
    constraint message_msg_id_uindex
        unique (msg_id)
)
    comment '消息表（）';

