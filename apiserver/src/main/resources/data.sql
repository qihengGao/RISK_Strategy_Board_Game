INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_MODERATOR');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');

INSERT INTO users(email,password,username) VALUES('test@test.com','$2a$10$mR4MU5esBbUd6JWuwWKTA.tRy.jo4d4XRkgnamcOJfw5pJ8Ao/RDS','test');

INSERT INTO user_roles(user_id,role_id) VALUES ('1','1');

INSERT INTO users(email,password,username) VALUES('test2@test2.com','$2a$10$mR4MU5esBbUd6JWuwWKTA.tRy.jo4d4XRkgnamcOJfw5pJ8Ao/RDS','test2');

INSERT INTO user_roles(user_id,role_id) VALUES ('2','1');