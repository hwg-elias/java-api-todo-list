CREATE TABLE todos (
  id TEXT PRIMARY KEY UNIQUE NOT NULL,
  title VARCHAR(16) NOT NULL,
  description VARCHAR(255) DEFAULT NULL,
  completed BOOLEAN NOT NULL DEFAULT FALSE
);