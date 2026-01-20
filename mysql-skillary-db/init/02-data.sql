USE testdb;

-- 이미 존재하면 무시하고, 없으면 넣음
INSERT IGNORE INTO roles (`role`) VALUES ('ROLE_USER');
INSERT IGNORE INTO roles (`role`) VALUES ('ROLE_ADMIN');
INSERT IGNORE INTO roles (`role`) VALUES ('ROLE_CREATOR');