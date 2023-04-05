create table if not exists apply_class_member
(
    apply_id     bigint                     not null comment '主键'
        primary key,
    open_id      varchar(200)               null comment '用户id',
    class_id     bigint                     null comment '申请加入班级的id',
    apply_time   datetime                   null comment '申请加入时间',
    type         varchar(20)                null comment '身份',
    result       varchar(20) default '正在审核' null comment '申请结果',
    deal         tinyint(1)  default 0      null comment '是否处理',
    deal_open_id varchar(200)               null comment '处理人id',
    constraint apply_class_member_apply_id_uindex
        unique (apply_id)
);

