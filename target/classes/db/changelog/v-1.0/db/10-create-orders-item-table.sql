create table orders_item
(
    id          serial not null,
    cost        int8,
    date_create date,
    status      varchar(255),
    uuid        uuid,
    user_id     integer references users,
    primary key (id)
);
