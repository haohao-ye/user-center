DROP TABLE IF EXISTS user;


-- auto-generated definition
create table user
(
    id            bigint auto_increment comment '主键id'
        primary key,
    user_account  varchar(256)                       not null comment '账号',
    user_password varchar(256)                       not null comment '密码',
    username      varchar(256)                       null comment '昵称',
    avatar_url    varchar(1024)                      null comment '用户头像',
    gender        tinyint                            null comment '性别',
    phone         varchar(128)                       null comment '电话',
    email         varchar(256)                       null comment '邮箱',
    user_role     tinyint  default 0                 not null comment '0--普通用户，1--管理员',
    is_invalid    tinyint  default 0                 not null comment '状态(0有效)',
    user_status   tinyint  default 0                 not null comment '用户状态（0正常）',
    create_time   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted    tinyint  default 0                 not null comment '逻辑删除（0未删除）'
)
    comment '用户表';

