INSERT INTO BOXES (ID, FREQUENCY) VALUES (1, 1), (2, 2), (3, 4), (4, 8), (5, 16), (6, 32), (7, 64);

INSERT INTO FLASHCARDS (QUESTION, ANSWER, TAGS, BOX_ID) VALUES
('question 1', 'answer 1', ('Geography'), 0),
('question 2', 'answer 2', ('Programming'), 2),
('question 3', 'answer 3', ('Java'), 2),
('question 4', 'answer 4', ('Maths'), 4),
('question 5', 'answer 5', ('Geography'), 6);

INSERT INTO SESSIONS (ID, SESSION_NUMBER) VALUES (1, 0)
