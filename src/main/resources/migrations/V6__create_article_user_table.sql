CREATE TABLE article_user (
    article_id VARBINARY(16) NOT NULL,
    user_id VARBINARY(16) NOT NULL,
    CONSTRAINT article_user_id_pk PRIMARY KEY (article_id, user_id),
    CONSTRAINT article_user_article_id_fk FOREIGN KEY (article_id) REFERENCES article (id),
    CONSTRAINT article_user_user_id_fk FOREIGN KEY (user_id) REFERENCES `user` (id)
) DEFAULT CHARSET=utf8;