-- auto-generated definition
create table post
(
    post_id            bigint auto_increment comment '게시물 고유번호'
        primary key,
    created_date       datetime(6)  null comment '생성일',
    last_modified_date datetime(6)  null comment '수정일',
    created_by         bigint null comment '생성자 고유번호',
    last_modified_by   bigint null comment '수정자 고유번호',
    content            longtext     null comment '게시물 내용',
    hits               int          not null comment '조회수',
    title              varchar(255) null comment '게시물 제목'
);

