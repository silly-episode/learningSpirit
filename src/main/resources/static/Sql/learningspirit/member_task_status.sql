create table if not exists member_task_status
(
    status_id   bigint                    not null comment '任务情况id'
        primary key,
    task_id     bigint                    null comment '任务id',
    open_id     varchar(200)              null comment '用户id',
    status      varchar(50) default '未完成' null comment '个人任务完成情况',
    status_time datetime                  null comment '状态时间',
    class_id    bigint                    null comment '班级id',
    rate        int         default 0     null comment '评星',
    msg         varchar(2000)             null comment '学生留言',
    remark      varchar(2000)             null comment '老师评语',
    file_list   varchar(2000)             null comment '文件列表 以逗号区分',
    mark_status tinyint(1)  default 0     null comment '老师是否批改',
    type        varchar(20)               null comment '任务类型，接龙，填表，通知；测验，作业',
    confirm     tinyint(1)  default 0     null comment '接龙和通知的确认',
    grade       int         default 0     null comment '测验和考试的成绩',
    answer_list varchar(3000)             null comment '填表的答案列表，用逗号隔开',
    json_str    varchar(10000)            null comment '问题、正确答案、学生答案'
)
    comment '个人任务完成情况';

