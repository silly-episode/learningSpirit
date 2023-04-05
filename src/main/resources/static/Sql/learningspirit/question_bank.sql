create table if not exists question_bank
(
    question_id          bigint        not null comment '题目id'
        primary key,
    subject_kind         varchar(20)   null comment '科目类型',
    question_kind        varchar(20)   null comment '题目类型，dan、duo、pan',
    content              varchar(1500) null comment '题目内容',
    choice               varchar(1000) null comment '选项 A:主题 B:数学 C:括号',
    answer               varchar(20)   null comment '答案',
    question_create_time datetime      null comment '创建时间',
    order_id             int           null comment '排序id',
    module               varchar(100)  null,
    module_id            bigint        null,
    upload_id            varchar(200)  null,
    constraint question_bank_question_id_uindex
        unique (question_id)
)
    comment '题库';

create index question_bank_order_id_index
    on question_bank (order_id);

