package com.blackpantech.leitner_system_automaton.domain.flashcards.exceptions;

/**
 * Checked exception when a fetched flashcard is not found
 */
public class FlashcardNotFoundException extends Exception {

    public FlashcardNotFoundException(final long id) {
        super(String.format("Flashcard with ID %d not found", id));
    }

}
