package com.blackpantech.leitner_system_automaton.domain.flashcards.exceptions;

/**
 * Checked exception when a fetched flashcard is not found
 */
public class BoxNotFoundException extends Exception {

    public BoxNotFoundException(final long id) {
        super(String.format("Box with ID %d not found", id));
    }

}
