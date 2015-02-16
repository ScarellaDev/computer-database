drop table if exists user;

CREATE TABLE user 
( id integer not null auto_increment
, username 			varchar(50) unique
, password	varchar(50)
, enabled 			boolean
, role 				varchar(50)
, constraint pk_company primary key (id));

INSERT INTO user VALUES (1, 'admin', 'admin', TRUE, 'ROLE_ADMIN');