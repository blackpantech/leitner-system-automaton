package com.blackpantech.leitner_system_automaton.domain.flashcards;

/**
 * Domain object for the box in which flashcards are stored
 *
 * @param id box id
 * @param frequency box frequency at which flashcards should be studied
 */
public record Box(long id, int frequency) {

}
