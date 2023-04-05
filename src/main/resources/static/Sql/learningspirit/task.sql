create table if not exists task
(
    task_id            bigint               not null
        primary key,
    open_id            varchar(200)         null comment '发布人id',
    type               varchar(50)          null comment '任务类型：班级事务(affair)：notice、jielong、tianbiao
学习任务（learn）：work、exam',
    title              varchar(200)         null comment '任务标题 测试作业标题	',
    content            varchar(1000)        null comment '测试作业内容	任务内容',
    file_list          varchar(1500)        null comment '文件列表 ,隔开',
    question_list      varchar(1500)        null comment '问题列表   ["学生姓名", "性别", "请输入手机号"]	只有部分填表类型的任务有该字段',
    way                varchar(100)         null comment '只有作业类型和部分填表类型的任务有该字段

文字、图片、视频、文件、无需在线提交（中文字段可以吗？不行的话我再改成英文）',
    receive_class_list varchar(1500)        null comment '接收的班级id列表 ,隔开',
    deadline           datetime             null comment '截至时间',
    fix_time           datetime             null comment '定时发布时间   为空即不定时，现在就发布',
    is_draft           tinyint(1) default 1 null comment '任务类型',
    q_number           int                  null comment '题目数量',
    random             tinyint(1) default 0 null comment '题目是否随机',
    module_id          bigint               null comment '题库id',
    publish_time       datetime             null comment '发布时间',
    constraint task_task_id_uindex
        unique (task_id)
)
    comment '任务表';

