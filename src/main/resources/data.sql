INSERT INTO BOXES (ID, FREQUENCY) VALUES
(0, 1),
(1, 2),
(2, 4),
(3, 8),
(4, 16),
(5, 32),
(6, 64);

INSERT INTO FLASHCARDS (QUESTION, ANSWER, TAGS, BOX_ID) VALUES
('question 1', 'answer 1', ('Geography'), 0),
('question 2', 'answer 2', ('Programming'), 2),
('question 3', 'answer 3', ('Java'), 2),
('question 4', 'answer 4', ('Maths'), 4),
('question 5', 'answer 5', ('Geography'), 6);
