CREATE TABLE article_tag (
    article_id VARBINARY(16) NOT NULL,
    tag_id VARBINARY(16) NOT NULL,
    CONSTRAINT article_tag_id_pk PRIMARY KEY (article_id, tag_id),
    CONSTRAINT article_id_fk FOREIGN KEY (article_id) REFERENCES article (id),
    CONSTRAINT tag_id_fk FOREIGN KEY (tag_id) REFERENCES tag (id)
) DEFAULT CHARSET=utf8;