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

