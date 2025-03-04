CREATE SEQUENCE client_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE address_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE phone_id_seq START WITH 1 INCREMENT BY 1;

create table addresses
(
    id bigint not null primary key,
    street varchar(50)
);

create table client
(
    id bigint not null primary key,
    name varchar(50),
    address_id BIGINT,
           FOREIGN KEY (address_id) REFERENCES addresses(id)
);
create table phones
(
    id   bigint not null primary key,
    number varchar(50),
    client_id BIGINT,
    FOREIGN KEY (client_id) REFERENCES client(id)
);




