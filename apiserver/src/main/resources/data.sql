INSERT INTO roles(name)
VALUES ('ROLE_USER');
INSERT INTO roles(name)
VALUES ('ROLE_MODERATOR');
INSERT INTO roles(name)
VALUES ('ROLE_ADMIN');

INSERT IGNORE INTO users(email, password, username, elo)
VALUES ('test@test.com', '$2a$10$mR4MU5esBbUd6JWuwWKTA.tRy.jo4d4XRkgnamcOJfw5pJ8Ao/RDS', 'test', 100);

INSERT IGNORE INTO user_roles(user_id, role_id)
VALUES ('1', '1');

INSERT IGNORE INTO users(email, password, username, elo)
VALUES ('test2@test2.com', '$2a$10$mR4MU5esBbUd6JWuwWKTA.tRy.jo4d4XRkgnamcOJfw5pJ8Ao/RDS', 'test2', 200);

INSERT IGNORE INTO user_roles(user_id, role_id)
VALUES ('2', '1');

INSERT IGNORE INTO users(email, password, username, elo)
VALUES ('test3@test3.com', '$2a$10$mR4MU5esBbUd6JWuwWKTA.tRy.jo4d4XRkgnamcOJfw5pJ8Ao/RDS', 'test3', 300);

INSERT IGNORE INTO user_roles(user_id, role_id)
VALUES ('3', '1');

INSERT IGNORE INTO users(email, password, username, elo)
VALUES ('test4@test5.com', '$2a$10$mR4MU5esBbUd6JWuwWKTA.tRy.jo4d4XRkgnamcOJfw5pJ8Ao/RDS', 'test4', 400);

INSERT IGNORE INTO user_roles(user_id, role_id)
VALUES ('4', '1');

INSERT IGNORE INTO users(email, password, username, elo)
VALUES ('test5@test5.com', '$2a$10$mR4MU5esBbUd6JWuwWKTA.tRy.jo4d4XRkgnamcOJfw5pJ8Ao/RDS', 'test5', 500);

INSERT IGNORE INTO user_roles(user_id, role_id)
VALUES ('5', '1');