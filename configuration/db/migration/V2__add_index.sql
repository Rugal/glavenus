SET search_path = pt;

CREATE UNIQUE INDEX user_email_unique_index ON "user" (email);
CREATE UNIQUE INDEX user_username_unique_index ON "user" (username);

CREATE INDEX post_tag_pid_index ON post_tag (pid);
CREATE INDEX post_tag_tid_index ON post_tag (tid);

CREATE INDEX review_pid_index ON review (pid);
CREATE INDEX review_uid_index ON review (uid);
