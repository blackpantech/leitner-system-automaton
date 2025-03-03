package com.blackpantech.leitner_system_automaton.domain.flashcards;

import com.blackpantech.leitner_system_automaton.domain.boxes.Box;

/**
 * Domain object for flashcards
 *
 * @param id flashcard id
 * @param question question to study
 * @param answer answer to the question
 * @param tags tags to organize flashcards (by theme for example)
 * @param currentBox current box where the flashcard is stored
 */
public record Flashcard(long id, String question, String answer, String[] tags, Box currentBox) {

}
