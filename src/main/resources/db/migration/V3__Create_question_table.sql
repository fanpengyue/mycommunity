CREATE TABLE question
(
    id int AUTO_INCREMENT PRIMARY KEY,
    title varchar(50),
    description text,
    gmt_create bigint,
    gmt_modified bigint,
    omment_count int DEFAULT 0,
    view_count int DEFAULT 0,
    like_count int DEFAULT 0,
    tag varchar(255)
);