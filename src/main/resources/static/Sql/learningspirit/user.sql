create table if not exists user
(
    open_id       varchar(100)  not null comment 'openId'
        primary key,
    user_name     varchar(20)   null comment '用户名',
    role          varchar(20)   null comment '角色',
    sex           varchar(10)   null comment '性别
',
    tel           varchar(20)   null comment '电话',
    subject       varchar(500)  null comment '所教的科目，多个科目之间用,区别',
    email         varchar(100)  null,
    register_time datetime      null comment '注册时间',
    user_status   int default 0 null comment '0正常，1锁定',
    constraint user_open_id_uindex
        unique (open_id)
)
    comment '用户表';

