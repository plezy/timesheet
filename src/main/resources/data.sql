INSERT INTO USERS (ID, USERNAME, PASSWORD, FIRSTNAME, LASTNAME, LOCKED, DELETED) VALUES (1, 'admin', '$2a$10$v2LQkXM4tqtNZQ6pvl5h7ORtrWYsEA3VGinU.ykZmQTIE5zcboNZG', 'Application', 'administrator', 'N', 'N');

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

INSERT INTO USERS (ID, USERNAME, PASSWORD, FIRSTNAME, LASTNAME, LOCKED, DELETED) VALUES (2, 'user', '$2a$10$v2LQkXM4tqtNZQ6pvl5h7ORtrWYsEA3VGinU.ykZmQTIE5zcboNZG', 'Normal', 'user', 'N', 'N');
INSERT INTO USER_ROLES (USR_ID, ROLE) VALUES (2, 'MANAGE_USERS');

INSERT INTO USERS (ID, USERNAME, PASSWORD, FIRSTNAME, LASTNAME, LOCKED, DELETED) VALUES (3, 'locked', '$2a$10$v2LQkXM4tqtNZQ6pvl5h7ORtrWYsEA3VGinU.ykZmQTIE5zcboNZG', 'Normal', 'locked', 'Y', 'N');
INSERT INTO USER_ROLES (USR_ID, ROLE) VALUES (3, 'MANAGE_USERS');

INSERT INTO USERS (ID, USERNAME, PASSWORD, FIRSTNAME, LASTNAME, LOCKED, DELETED) VALUES (4, 'project', '$2a$10$v2LQkXM4tqtNZQ6pvl5h7ORtrWYsEA3VGinU.ykZmQTIE5zcboNZG', 'Project', 'user', 'N', 'N');
INSERT INTO USER_ROLES (USR_ID, ROLE) VALUES (4, 'ENTER_TIME_TRACK');
INSERT INTO USER_ROLES (USR_ID, ROLE) VALUES (4, 'GENERATE_REPORT');
INSERT INTO USER_ROLES (USR_ID, ROLE) VALUES (4, 'MANAGE_PROJECTS');

INSERT INTO USERS (ID, USERNAME, PASSWORD, FIRSTNAME, LASTNAME, LOCKED, DELETED) VALUES (5, 'deleted', '$2a$10$v2LQkXM4tqtNZQ6pvl5h7ORtrWYsEA3VGinU.ykZmQTIE5zcboNZG', 'User', 'deleted', 'N', 'Y');
INSERT INTO USER_ROLES (USR_ID, ROLE) VALUES (5, 'MANAGE_SETTINGS');

