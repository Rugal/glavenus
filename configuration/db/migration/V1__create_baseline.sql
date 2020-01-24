--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.4
-- Dumped by pg_dump version 9.5.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: test; Type: SCHEMA; Schema: -; Owner: -
--

SET search_path = pt;
SET default_tablespace = '';
SET default_with_oids = false;

CREATE TABLE "user" (
  uid serial PRIMARY KEY,
  username character varying(50),
  password character varying(100),
  email character varying(50),
  secret character varying(50),
  download bigint,
  upload bigint,
  credit int,
  status int,
  create_at bigint,
  update_at bigint
);

CREATE TABLE post (
  pid serial PRIMARY KEY,
  title character varying(100),
  content text,
  enable boolean,
  hash character varying(50),
  torrent bytea,
  size bigint,
  create_at bigint,
  update_at bigint,
  author integer REFERENCES "user"(uid)
);

CREATE TABLE client (
  cid serial PRIMARY KEY,
  name character varying(50),
  version character varying(10),
  enable boolean,
  create_at bigint,
  update_at bigint
);

CREATE TABLE announce (
  aid serial PRIMARY KEY,
  download bigint,
  upload bigint,
  post integer REFERENCES post(pid),
  "user" integer REFERENCES "user"(uid),
  client integer REFERENCES client(cid),
  create_at bigint,
  update_at bigint
);

CREATE TABLE tag (
  tid serial PRIMARY KEY,
  name character varying(50),
  create_at bigint,
  update_at bigint
);

CREATE TABLE post_tag (
  ptid serial PRIMARY KEY,
  tid integer REFERENCES tag(tid),
  pid integer REFERENCES post(pid),
  create_at bigint,
  update_at bigint
);

CREATE TABLE review (
  rid serial PRIMARY KEY,
  uid integer REFERENCES "user"(uid),
  pid integer REFERENCES post(pid),
  content text,
  rate integer,
  create_at bigint,
  update_at bigint
);

--
-- PostgreSQL database dump complete
--

