-- auto-generated definition
create table delivery
(
    delivery_id     bigint auto_increment comment '배송 고유번호'
        primary key,
    created_date       datetime(6)  null comment '생성일',
    last_modified_date datetime(6)  null comment '수정일',
    created_by         bigint null comment '생성자 고유번호',
    last_modified_by   bigint null comment '수정자 고유번호',
    address         varchar(255) null comment '주소',
    zipcode         char(5) null comment '우편번호',
    member_id       bigint       null comment '멤버 고유번호',
    order_id        bigint       null comment '주문 고유번호',
    delivery_status varchar(15) null comment '배송 상태'
);

-- auto-generated definition
create table delivery_rider
(
    delivery_rider_id   bigint auto_increment comment '배송 라이더 고유번호'
        primary key,
    created_date        datetime(6)  null comment '생성일',
    last_modified_date  datetime(6)  null comment '수정일',
    created_by          bigint       null comment '생생자 고유번호',
    last_modified_by    bigint       null comment '수정자 고유번호',
    phone_number_first  varchar(3)   null comment '휴대폰번호 첫자리',
    phone_number_last   varchar(4)   null comment '휴대폰번호 중간자리',
    phone_number_middle varchar(4)   null comment '휴대폰번호 마지막자리',
    rider_id            bigint       null comment '라이더 고유번호',
    rider_name          varchar(255) null comment '라이더 이름',
    delivery_id         bigint       null comment '배송 고유번호',
    constraint FK8h32gnlrewv93wfwvd8g4merr
        foreign key (delivery_id) references delivery (delivery_id)
);

