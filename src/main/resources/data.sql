INSERT INTO BOXES (ID, FREQUENCY) VALUES (1, 1), (2, 2), (3, 4), (4, 8), (5, 16), (6, 32), (7, 64);

INSERT INTO FLASHCARDS (QUESTION, ANSWER, TAGS, BOX_ID) VALUES
('question 1', 'answer 1', ('Geography'), 1),
('question 2', 'answer 2', ('Programming'), 3),
('question 3', 'answer 3', ('Java'), 3),
('question 4', 'answer 4', ('Maths'), 5),
('question 5', 'answer 5', ('Geography'), 7);

INSERT INTO SESSIONS (ID, SESSION_NUMBER) VALUES (1, 0)
