package com.blackpantech.leitner_system_automaton.domain.flashcards;

import com.blackpantech.leitner_system_automaton.domain.boxes.Box;
import com.blackpantech.leitner_system_automaton.domain.boxes.exceptions.BoxNotFoundException;
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
    Flashcard editFlashcard(final long id,
                            final String question,
                            final String answer,
                            final String[] tags,
                            final Box currentBox)
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

    /**
     * Gets a list of all flashcards from a box, given the box ID
     *
     * @param boxId ID of the box to flashcards from
     *
     * @return list of all flashcards from a box
     */
    List<Flashcard> getAllFlashcardsFromBox(final long boxId) throws BoxNotFoundException;

    /**
     * Gets a list of all flashcards from a box, given the box ID and a flashcard tag
     *
     * @param boxId ID of the box to flashcards from
     * @param tag tag to filter all flashcards from box
     *
     * @return list of all flashcards from a box
     */
    List<Flashcard> getAllFlashcardsWithTagFromBox(final long boxId, final String tag) throws BoxNotFoundException;

}
