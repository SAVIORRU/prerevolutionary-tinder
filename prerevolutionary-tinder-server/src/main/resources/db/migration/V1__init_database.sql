DROP TABLE IF EXISTS likes;
DROP TABLE IF EXISTS presons;
CREATE TABLE IF NOT EXISTS likes
(
    liked_by     BIGINT NOT NULL,
    liked_person BIGINT NOT NULL,
    CONSTRAINT pk_likes PRIMARY KEY (liked_by, liked_person)
);

CREATE TABLE IF NOT EXISTS persons
(
    id          BIGINT NOT NULL,
    name        VARCHAR(255),
    gender      INTEGER,
    description VARCHAR(2048),
    CONSTRAINT pk_persons PRIMARY KEY (id)
);

ALTER TABLE likes
    ADD CONSTRAINT KlJg02 FOREIGN KEY (liked_by) REFERENCES persons (id);

ALTER TABLE likes
    ADD CONSTRAINT KlJg0g FOREIGN KEY (liked_person) REFERENCES persons (id);