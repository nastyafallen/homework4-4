-- liquibase formatted sql

-- changeset nastyafallen: 1
CREATE INDEX students_name_index ON student (name);

-- changeset nastyafallen: 2
CREATE INDEX faculties_name_and_color_index ON faculty (name, color);
