create table if not exists login_log
(
    log_id     bigint       not null
        primary key,
    login_time datetime     null,
    open_id    varchar(100) null comment '登录账户',
    nick_name  varchar(100) null comment '用户名',
    log_remark varchar(300) null comment '结果的描述',
    role       varchar(255) null comment '角色',
    constraint loginLog_logId_uindex
        unique (log_id)
);

