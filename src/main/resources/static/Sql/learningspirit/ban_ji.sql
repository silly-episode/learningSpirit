create table if not exists ban_ji
(
    class_id          bigint        not null
        primary key,
    class_name        varchar(200)  null,
    class_num         int default 0 null,
    class_creator     varchar(100)  null comment '创建者',
    class_admin       varchar(100)  null comment '管理员，可转让，创建者为默认管理员',
    class_create_time datetime      null,
    joined            int default 1 null comment '已经加入班级人数',
    teacher_count     int default 1 null,
    constraint class_class_id_uindex
        unique (class_id)
)
    comment '班级表';

