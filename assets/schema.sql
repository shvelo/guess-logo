CREATE TABLE brands(
	_id INTEGER PRIMARY KEY,
	name TEXT,
	logo TEXT,
	variant1 TEXT,
	variant2 TEXT,
	variant3 TEXT,
	variant4 TEXT,
	correct INTEGER,
	guessed INTEGER
);
CREATE TABLE userdata(
  _id INTEGER PRIMARY KEY,
  name TEXT,
  value TEXT
);
INSERT INTO userdata(name,value) VALUES('score',0);
