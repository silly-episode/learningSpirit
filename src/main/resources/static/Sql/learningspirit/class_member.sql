create table if not exists class_member
(
    class_id bigint                        not null,
    open_id  varchar(300)                  not null,
    type     varchar(20) default 'student' null,
    primary key (class_id, open_id)
)
    comment '班级成员表';

