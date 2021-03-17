create table accounts (
    account_id serial primary key,
    account_name varchar(255),
    account_tel varchar(255)
);

create table seats (
    seat_id serial primary key,
    seat_row int,
    seat_col int,
    price int,
    account_id int references accounts(account_id)
);