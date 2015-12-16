# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table category (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_category primary key (id))
;

create table product (
  id                        bigint not null,
  category_id               bigint,
  name                      varchar(255),
  description               varchar(255),
  stock                     integer,
  price                     double,
  constraint pk_product primary key (id))
;

create table user (
  email                     varchar(255) not null,
  name                      varchar(255),
  password                  varchar(255),
  role                      varchar(255),
  constraint pk_user primary key (email))
;

create sequence category_seq;

create sequence product_seq;

create sequence user_seq;

alter table product add constraint fk_product_category_1 foreign key (category_id) references category (id) on delete restrict on update restrict;
create index ix_product_category_1 on product (category_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists category;

drop table if exists product;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists category_seq;

drop sequence if exists product_seq;

drop sequence if exists user_seq;

