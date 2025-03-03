package com.blackpantech.leitner_system_automaton.domain.flashcards;

import com.blackpantech.leitner_system_automaton.domain.flashcards.exceptions.FlashcardNotFoundException;

import java.util.List;

/**
 * Flashcards repository interface
 */
public interface FlashcardsRepository {

    /**
     * Creates a flashcard
     *
     * @param question flashcard's question
     * @param answer flashcard's answer
     * @param tags flashcard's tags
     *
     * @return created flashcard
     */
    Flashcard createFlashcard(final String question, final String answer, final String[] tags);

    /**
     * Edits a flashcard with given ID
     *
     * @param id flashcard's ID to edit
     * @param question edited flashcard's question
     * @param answer edited flashcard's answer
     * @param tags edited flashcard's tags
     *
     * @return edited flashcard
     *
     * @throws FlashcardNotFoundException if no flashcard has given ID
     */
    Flashcard editFlashcard(final long id, final String question, final String answer, final String[] tags)
            throws FlashcardNotFoundException;

    /**
     * Gets a flashcard with given ID
     *
     * @param id flashcard's ID to get
     *
     * @return flashcard with given ID
     *
     * @throws FlashcardNotFoundException if no flashcard has given ID
     */
    Flashcard getFlashcard(final long id) throws FlashcardNotFoundException;

    /**
     * Deletes a  flashcard with given ID
     *
     * @param id flashcard's ID to delete
     *
     * @throws FlashcardNotFoundException if no flashcard has given ID
     */
    void deleteFlashcard(final long id) throws FlashcardNotFoundException;

    /**
     * Gets a list of all flashcards
     *
     * @return list of all flashcards
     */
    List<Flashcard> getAllFlashcards();

    /**
     * Gets a list of all flashcards
     *
     * @param tag tag to filter all flashcards
     *
     * @return list of all flashcards with given tag
     */
    List<Flashcard> getAllFlashcardsWithTag(final String tag);

}
