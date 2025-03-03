package com.blackpantech.leitner_system_automaton.domain.flashcards;

import com.blackpantech.leitner_system_automaton.domain.boxes.Box;
import com.blackpantech.leitner_system_automaton.domain.flashcards.exceptions.FlashcardNotFoundException;

import java.util.List;

/**
 * Domain service to create, get, edit or delete flashcards stored in the repository
 */
public class FlashcardsService {

    private final FlashcardsRepository flashcardsRepository;

    public FlashcardsService(final FlashcardsRepository flashcardsRepository) {
        this.flashcardsRepository = flashcardsRepository;
    }

    /**
     * Creates a flashcard
     *
     * @param question flashcard's question
     * @param answer flashcard's answer
     * @param tags flashcard's tags
     *
     * @return created flashcard
     */
    public Flashcard createFlashcard(final String question, final String answer, final String[] tags) {
        return flashcardsRepository.createFlashcard(question, answer, tags);
    }

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
    public Flashcard editFlashcard(final long id,
                                   final String question,
                                   final String answer,
                                   final String[] tags,
                                   final Box box)
            throws FlashcardNotFoundException {
        return flashcardsRepository.editFlashcard(id, question, answer, tags, box);
    }

    /**
     * Gets a flashcard with given ID
     *
     * @param id flashcard's ID to get
     *
     * @return flashcard with given ID
     *
     * @throws FlashcardNotFoundException if no flashcard has given ID
     */
    public Flashcard getFlashcard(final long id) throws FlashcardNotFoundException {
        return flashcardsRepository.getFlashcard(id);
    }

    /**
     * Deletes a  flashcard with given ID
     *
     * @param id flashcard's ID to delete
     *
     * @throws FlashcardNotFoundException if no flashcard has given ID
     */
    public void deleteFlashcard(final long id) throws FlashcardNotFoundException {
        flashcardsRepository.deleteFlashcard(id);
    }

    /**
     * Gets a list of all flashcards
     *
     * @return list of all flashcards
     */
    public List<Flashcard> getAllFlashcards() {
        return flashcardsRepository.getAllFlashcards();
    }

    /**
     * Gets a list of all flashcards
     *
     * @param tag tag to filter all flashcards
     *
     * @return list of all flashcards with given tag
     */
    public List<Flashcard> getAllFlashcardsWithTag(final String tag) {
        return flashcardsRepository.getAllFlashcardsWithTag(tag);
    }

}
