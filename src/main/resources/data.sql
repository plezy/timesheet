INSERT INTO USERS (ID, USERNAME, PASSWORD, FIRSTNAME, LASTNAME, EMAIL, LOCKED, DELETED) VALUES (1, 'admin', '$2a$10$v2LQkXM4tqtNZQ6pvl5h7ORtrWYsEA3VGinU.ykZmQTIE5zcboNZG', 'Application', 'administrator', 'admin@localhost', 'N', 'N');

INSERT INTO USER_ROLES (USR_ID, ROLE) VALUES (1, 'ENTER_TIME_TRACK');
INSERT INTO USER_ROLES (USR_ID, ROLE) VALUES (1, 'ENTER_OTHERS_TIME_TRACK');
INSERT INTO USER_ROLES (USR_ID, ROLE) VALUES (1, 'GENERATE_REPORT');
INSERT INTO USER_ROLES (USR_ID, ROLE) VALUES (1, 'PREPARE_INVOICE');
INSERT INTO USER_ROLES (USR_ID, ROLE) VALUES (1, 'MANAGE_CUSTOMERS');
INSERT INTO USER_ROLES (USR_ID, ROLE) VALUES (1, 'MANAGE_PROJECTS');
INSERT INTO USER_ROLES (USR_ID, ROLE) VALUES (1, 'MANAGE_USERS');
INSERT INTO USER_ROLES (USR_ID, ROLE) VALUES (1, 'MANAGE_BILLING_TYPES');
INSERT INTO USER_ROLES (USR_ID, ROLE) VALUES (1, 'MANAGE_WORK_SCHEDULE');
INSERT INTO USER_ROLES (USR_ID, ROLE) VALUES (1, 'MANAGE_SETTINGS');

INSERT INTO USERS (ID, USERNAME, PASSWORD, FIRSTNAME, LASTNAME, EMAIL, LOCKED, DELETED) VALUES (2, 'user', '$2a$10$v2LQkXM4tqtNZQ6pvl5h7ORtrWYsEA3VGinU.ykZmQTIE5zcboNZG', 'Normal', 'user', 'user@localhost', 'N', 'N');
INSERT INTO USER_ROLES (USR_ID, ROLE) VALUES (2, 'MANAGE_USERS');

INSERT INTO USERS (ID, USERNAME, PASSWORD, FIRSTNAME, LASTNAME, EMAIL, LOCKED, DELETED) VALUES (3, 'locked', '$2a$10$v2LQkXM4tqtNZQ6pvl5h7ORtrWYsEA3VGinU.ykZmQTIE5zcboNZG', 'Normal', 'locked', 'locked@localhost', 'Y', 'N');
INSERT INTO USER_ROLES (USR_ID, ROLE) VALUES (3, 'MANAGE_USERS');

INSERT INTO USERS (ID, USERNAME, PASSWORD, FIRSTNAME, LASTNAME, EMAIL, LOCKED, DELETED) VALUES (4, 'project', '$2a$10$v2LQkXM4tqtNZQ6pvl5h7ORtrWYsEA3VGinU.ykZmQTIE5zcboNZG', 'Project', 'project', 'project@localhost', 'N', 'N');
INSERT INTO USER_ROLES (USR_ID, ROLE) VALUES (4, 'ENTER_TIME_TRACK');
INSERT INTO USER_ROLES (USR_ID, ROLE) VALUES (4, 'GENERATE_REPORT');
INSERT INTO USER_ROLES (USR_ID, ROLE) VALUES (4, 'MANAGE_PROJECTS');

INSERT INTO USERS (ID, USERNAME, PASSWORD, FIRSTNAME, LASTNAME, EMAIL, LOCKED, DELETED) VALUES (5, 'deleted', '$2a$10$v2LQkXM4tqtNZQ6pvl5h7ORtrWYsEA3VGinU.ykZmQTIE5zcboNZG', 'User', 'deleted', 'deleted@localhost', 'N', 'Y');
INSERT INTO USER_ROLES (USR_ID, ROLE) VALUES (5, 'MANAGE_SETTINGS');

INSERT INTO CUSTOMERS
(ID, ADDR_LINE1, ADDR_LINE2, ADDR_AREA, ADDR_CITY, ADDR_COUNTRY, ADDR_POSTCODE, ARCHIVED, BILL_ADDRLINE1,
BILL_ADDRLINE2, BILL_AREA, BILL_CITY, BILL_COUNTRY1, BILL_POSTCODE, CREATED_ON, DELETED, NAME, CREATED_BY, UPDATED_ON, UPDATED_BY, USE_BILLING_ADDR)
VALUES
(1, 'Adresse ligne 1', 'Adresse ligne 2', 'Area', 'City', 'France', '12345', 'N', 'Bill adresse ligne 1', 'Bill adresse ligne 2',
'Bill area', 'BillingVille', 'ITALIE', 'I-54321', '2019-01-01 00:00:00', 'N', 'Customer B name', 2, '2019-01-01 00:00:00', 2, 'Y');

INSERT INTO CUSTOMERS
(ID, ADDR_LINE1, ADDR_LINE2, ADDR_AREA, ADDR_CITY, ADDR_COUNTRY, ADDR_POSTCODE, ARCHIVED, BILL_ADDRLINE1,
BILL_ADDRLINE2, BILL_AREA, BILL_CITY, BILL_COUNTRY1, BILL_POSTCODE, CREATED_ON, DELETED, NAME, CREATED_BY, UPDATED_ON, UPDATED_BY, USE_BILLING_ADDR)
VALUES
(2, 'Adresse 2 ligne 1', 'Adresse 2 ligne 2', 'Area', 'City', 'Belgique', '1234', 'N', 'Bill adresse 2 ligne 1', 'Bill adresse 2 ligne 2',
'Bill area', 'BillingVille', 'Belgique', '1234', '2019-01-01 00:00:00', 'N', 'Customer A name', 3, '2019-01-01 00:00:00', 3, 'Y');
