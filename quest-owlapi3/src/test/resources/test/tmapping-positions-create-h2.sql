
CREATE TABLE table1 (
    uri character varying(100) NOT NULL,
    val1 integer NOT NULL,
    val2 integer NOT NULL,
    val3 integer NOT NULL
);


INSERT INTO table1 VALUES ('uri1', 1, 0, 0);
INSERT INTO table1 VALUES ('uri2', 0, 1, 0);
INSERT INTO table1 VALUES ('uri3', 0, 0, 1);

ALTER TABLE table1
    ADD CONSTRAINT table1_pkey PRIMARY KEY (uri);

