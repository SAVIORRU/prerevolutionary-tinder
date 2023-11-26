DROP TABLE IF EXISTS person_search_preferences;
CREATE TABLE person_search_preferences
(
    person_id         BIGINT NOT NULL,
    preference_gender INTEGER
);

ALTER TABLE person_search_preferences
    ADD CONSTRAINT fk_person_search_preferences_on_person FOREIGN KEY (person_id) REFERENCES persons (id);