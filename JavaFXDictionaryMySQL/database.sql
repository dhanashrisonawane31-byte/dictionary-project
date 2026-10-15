CREATE DATABASE IF NOT EXISTS dictionary_db;
USE dictionary_db;

CREATE TABLE IF NOT EXISTS words (
    id INT PRIMARY KEY AUTO_INCREMENT,
    word VARCHAR(100) NOT NULL UNIQUE,
    meaning VARCHAR(500) NOT NULL,
    part_of_speech VARCHAR(50),
    example_sentence VARCHAR(500)
);

INSERT INTO words (word, meaning, part_of_speech, example_sentence)
VALUES
('computer', 'An electronic device that processes data.', 'Noun',
 'I use a computer for my college work.'),
('database', 'An organized collection of data stored electronically.', 'Noun',
 'My project stores student data in a database.'),
('program', 'A set of instructions given to a computer.', 'Noun',
 'Java is used to write computer programs.'),
('beautiful', 'Pleasant or attractive to look at.', 'Adjective',
 'The garden is beautiful.')
ON DUPLICATE KEY UPDATE word = VALUES(word);
