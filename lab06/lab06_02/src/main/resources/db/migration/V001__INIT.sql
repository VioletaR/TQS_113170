create table books (
   id serial primary key,
   name varchar(255) not null
);

INSERT INTO books  (name) VALUES ('Book 1'), ('Book 2'), ('Book 3');
