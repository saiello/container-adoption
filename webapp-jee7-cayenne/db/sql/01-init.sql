CREATE USER dbuser IDENTIFIED BY dbpassword;

GRANT CREATE TABLE TO dbuser;
GRANT CONNECT TO dbuser;
GRANT CREATE SESSION TO dbuser;
GRANT CREATE TABLE TO dbuser;
GRANT CREATE SEQUENCE TO dbuser;
GRANT UNLIMITED TABLESPACE TO dbuser;

connect dbuser/dbpassword


set sqlblanklines on;


CREATE TABLE dbuser.BLOG_POST (
	ID				NUMBER  			NOT NULL,
	TITLE			VARCHAR2 (50)		NOT NULL,
	DESCRIPTION		VARCHAR2 (50)		NOT NULL,
	CREATED		    DATE				NOT NULL,
	CONSTRAINT BLOG_PK PRIMARY KEY (ID)
);

CREATE SEQUENCE dbuser.BLOG_POST_SEQ
MINVALUE 1 MAXVALUE 9999999999999999999999999999 
INCREMENT BY 1 
START WITH 1 
CACHE 20 NOORDER  NOCYCLE;


INSERT INTO dbuser.BLOG_POST VALUES(1, 'Cayenne 101', 'Cayenne OracleDB datasource integration test', sysdate);