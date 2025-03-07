# leitner-system-automaton

## A Leitner learning system automaton

This Discord bot is a learning tool, following the Leitner system.

The Leitner system, named after Sebastian Leitner, is a learning method using flashcards.
It is a simple implementation of the principle of spaced repetition, where cards are reviewed at increasing intervals.

Flashcards are sorted into groups according to how well the learner knows each one in Leitner's learning box.
The learners try to recall the solution written on a flashcard. If they succeed, they send the card to the next group.
If they fail, they send it back to the first group.
Each succeeding group has a longer period before the learner is required to revisit the cards.

## Why I made this

I am currently looking for a new job and technical interviews can be difficult to succeed in.
This is why I made this project to help me study and prepare for interview questions and show recruiters my programming skills.

This project was a good way to put my programming skills in practice.
I made this bot following the TDD method, S.O.L.I.D principles and a hexagonal architecture.

The project is fully tested except for the API controller part it uses an external library to connect to Discord and is not easily mocked and unit testable.

The project is easily extensible and open to be redesigned with other techs (I could have used an HTTP controller for example).

## What you can do with this bot

There are two ways you can interact with this bot:
- You can create, edit, get and delete flashcards
- You can study flashcards and evaluate yourself to move them to another box if you answer them correctly or not

## How the bot works

Flashcards are stored in a relational database. There are two Spring profiles that you can run the bot with.
I recommend using the 'prod' profile for long term usage as it connects to a persistent PostgreSQL database (that you have to configure along the application).
The 'dev' profile is more geared towards development usage because it uses an in-memory H2 database.

## How to install the bot

### Prerequisites

### Installation

## How to use the bot
