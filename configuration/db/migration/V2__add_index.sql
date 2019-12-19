SET search_path = pt;

CREATE UNIQUE INDEX user_email_unique_index ON "user" (email);
CREATE UNIQUE INDEX user_username_unique_index ON "user" (username);
