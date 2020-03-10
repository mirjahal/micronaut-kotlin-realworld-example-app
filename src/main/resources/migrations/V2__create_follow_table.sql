CREATE TABLE IF NOT EXISTS user_follow (
  follower_user_id VARBINARY(16) NOT NULL,
  followed_user_id VARBINARY(16) NOT NULL,
  CONSTRAINT followers_id_pk PRIMARY KEY (follower_user_id, followed_user_id),
  CONSTRAINT follower_user_id_fk FOREIGN KEY (follower_user_id) REFERENCES `user` (id),
  CONSTRAINT followed_user_id_fk FOREIGN KEY (followed_user_id) REFERENCES `user` (id)
) DEFAULT CHARSET=utf8;