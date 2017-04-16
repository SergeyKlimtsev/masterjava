CREATE TABLE mail_status (
  id      INTEGER PRIMARY KEY DEFAULT nextval('common_seq'),
  "to"    TEXT                DEFAULT NULL,
  cc      TEXT                DEFAULT NULL,
  subject TEXT                DEFAULT NULL,
  body    TEXT                DEFAULT NULL,
  success BOOLEAN             DEFAULT NULL
);