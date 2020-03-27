CREATE DATABASE taskdb;

USE taskdb;

CREATE TABLE persons
(
	person_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	person_name VARCHAR(30) NOT NULL,
    person_age INT NOT NULL,
    person_birthday DATE NOT NULL
);

INSERT persons(person_name, person_age, person_birthday)
VALUES
("Tom", 22, "1997-12-01"),
("Sara", 27, "1992-01-01"),
("Henry", 38, "1981-03-23"),
("Batman", 104, "1915-04-17");