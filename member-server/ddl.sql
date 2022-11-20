-- auto-generated definition
create table member
(
    member_id           bigint auto_increment  comment '멤버 고유번호'
        primary key,
    created_date        datetime(6)  null comment '생성일',
    last_modified_date  datetime(6)  null  comment '수정일',
    created_by          bigint null  comment '생성자 고유번호',
    last_modified_by    bigint null  comment '수정자 고유번호',
    address             varchar(255) null  comment '주소',
    zipcode             char(5) null  comment '우편번호',
    auth_type           varchar(15) null  comment '로그인 유형',
    email               varchar(60) not null comment '이메일',
    name                varchar(60) null  comment '이름',
    password            varchar(60) not null  comment '비밀번호',
    phone_number_first  char(3)   null  comment '휴대폰번호 첫자리',
    phone_number_last   char(4)   null  comment '휴대폰번호 중간자리',
    phone_number_middle char(4)   null comment '휴대폰번호 마지막자리',
    role_type           varchar(15) null comment '권한 유형',
    constraint UK_mbmcqelty0fbrvxp1q58dn57t
        unique (email)
);