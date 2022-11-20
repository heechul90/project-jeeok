-- auto-generated definition
create table store
(
    store_id               bigint auto_increment comment '스토어 고유번호'
        primary key,
    created_date           datetime(6)  null comment '생성일',
    last_modified_date     datetime(6)  null comment '수정일',
    created_by             bigint null comment '생성자 고유번호',
    last_modified_by       bigint null comment '수정자 고유번호',
    address                char(5) null comment '주소',
    zipcode                varchar(255) null comment '우편번호',
    business_closing_hours char(4)      null comment '영업종료시간',
    business_opening_hours char(4)      null comment '영업시작시간',
    delete_yn              char(1)         null comment '삭제여부',
    member_id              bigint       null comment '멤버 고유번호',
    store_name             varchar(60) null comment '스토어명',
    phone_number_first     varchar(3)   null comment '휴대폰번호 첫자리',
    phone_number_last      varchar(4)   null comment '휴대폰번호 중간자리',
    phone_number_middle    varchar(4)   null comment '휴대폰번호 마지막자리'
);

-- auto-generated definition
create table category
(
    category_id        bigint auto_increment comment '카테고리 고유번호'
        primary key,
    created_date       datetime(6)  null comment '생성일',
    last_modified_date datetime(6)  null comment '수정일',
    created_by         bigint null comment '생성자 고유번호',
    last_modified_by   bigint null comment '수정자 고유번호',
    category_name      varchar(255) null comment '카테고리명',
    category_order     int          null comment '정렬번호',
    store_id           bigint       null comment '스토어 고유번호',
    constraint FKqdaotwkf5g9vmdox82ttt50i5
        foreign key (store_id) references store (store_id)
);

-- auto-generated definition
create table item
(
    item_id            bigint auto_increment comment '상품 고유번호'
        primary key,
    created_date       datetime(6)  null comment '생성일',
    last_modified_date datetime(6)  null comment '수정일',
    created_by         bigint null comment '생성자 고유번호',
    last_modified_by   bigint null comment '수정자 고유번호',
    item_name          varchar(60) null comment '상품명',
    photo_name         varchar(60) null comment '사진명',
    photo_path         varchar(120) null comment '사진경로',
    price              int          not null comment '상품가격',
    sales_yn           char(1) null comment '판매여부',
    category_id        bigint       null comment '카테고리 고유번호',
    store_id           bigint       null comment '스토어 고유번호',
    stock_quantity     int          not null comment '재고수량',
    constraint FK2n9w8d0dp4bsfra9dcg0046l4
        foreign key (category_id) references category (category_id),
    constraint FKi0c87m5jy5qxw8orrf2pugs6h
        foreign key (store_id) references store (store_id)
);

-- auto-generated definition
create table favorite_store
(
    favorite_store_id  bigint auto_increment comment '호감 스토어 고유번호'
        primary key,
    created_date       datetime(6)  null comment '생성일',
    last_modified_date datetime(6)  null comment '수정일',
    created_by         bigint null comment '생성자 고유번호',
    last_modified_by   bigint null comment '수정자 고유번호',
    member_id          bigint       null comment '멤버 고유번호',
    store_id           bigint       null comment '스토어 고유번호',
    constraint FKr6bk7q3ru7y3dqcis9exkpxef
        foreign key (store_id) references store (store_id)
);





