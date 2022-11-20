-- auto-generated definition
create table orders
(
    order_id           bigint auto_increment comment '주문 고유번호'
        primary key,
    created_date       datetime(6)  null comment '생성일',
    last_modified_date datetime(6)  null comment '수정일',
    created_by         bigint null comment '생성자 고유번호',
    last_modified_by   bigint null comment '수정자 고유번호',
    member_id          bigint       null comment '멤버 고유번호',
    order_price        int          not null comment '주문 가격',
    order_status       int          null comment '주문 상태',
    order_time         datetime(6)  null comment '주문 시간'
);

-- auto-generated definition
create table order_item
(
    order_item_id      bigint auto_increment comment '주문상품 고유번호'
        primary key,
    created_date       datetime(6)  null comment '생성일',
    last_modified_date datetime(6)  null comment '수정일',
    created_by         bigint null comment '생성자 고유번호',
    last_modified_by   bigint null comment '수정자 고유번호',
    count              int          not null comment '주문수량',
    item_id            bigint       null comment '상품 고유번호',
    price              int          not null comment '주문가격',
    order_id           bigint       null comment '주문 고유번호',
    constraint FKt4dc2r9nbvbujrljv3e23iibt
        foreign key (order_id) references orders (order_id)
);

