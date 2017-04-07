DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS cities;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS projects;
DROP TABLE IF EXISTS user_to_group_refs;

DROP SEQUENCE IF EXISTS user_seq;
DROP SEQUENCE IF EXISTS city_seq;
DROP SEQUENCE IF EXISTS group_seq;
DROP SEQUENCE IF EXISTS project_seq;

DROP TYPE IF EXISTS user_flag;
DROP TYPE IF EXISTS group_type;

CREATE TYPE user_flag AS ENUM ('active', 'deleted', 'superuser');
CREATE TYPE group_type AS ENUM ('FINISHED', 'CURRENT', 'REGISTERING');

CREATE SEQUENCE user_seq START 100000;
CREATE SEQUENCE city_seq START 100;
CREATE SEQUENCE group_seq START 100;
CREATE SEQUENCE project_seq START 1000;

CREATE TABLE users (
  id        INTEGER PRIMARY KEY DEFAULT nextval('user_seq'),
  full_name TEXT      NOT NULL,
  email     TEXT      NOT NULL,
  flag      user_flag NOT NULL,
  city_id   TEXT      NOT NULL
);

CREATE TABLE cities (
  id   TEXT PRIMARY KEY NOT NULL,
  name TEXT             NOT NULL UNIQUE
);
CREATE TABLE groups (
  id           INTEGER DEFAULT nextval('group_seq'),
  name         TEXT PRIMARY KEY   NOT NULL UNIQUE,
  type         group_type         NOT NULL,
  project_name TEXT               NOT NULL

);
CREATE TABLE user_to_group_refs (
  user_id    INTEGER NOT NULL,
  group_name TEXT    NOT NULL
);
CREATE TABLE projects (
  id          INTEGER DEFAULT nextval('project_seq'),
  name        TEXT PRIMARY KEY NOT NULL UNIQUE,
  description TEXT             NOT NULL
);
CREATE UNIQUE INDEX email_idx ON users (email);