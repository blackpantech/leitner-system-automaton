package com.blackpantech.leitner_system_automaton.domain.leitner_system;

import com.blackpantech.leitner_system_automaton.domain.boxes.Box;
import com.blackpantech.leitner_system_automaton.domain.boxes.BoxesRepository;
import com.blackpantech.leitner_system_automaton.domain.flashcards.Flashcard;
import com.blackpantech.leitner_system_automaton.domain.flashcards.FlashcardsRepository;
import com.blackpantech.leitner_system_automaton.domain.boxes.exceptions.BoxNotFoundException;
import com.blackpantech.leitner_system_automaton.domain.flashcards.exceptions.FlashcardNotFoundException;
import com.blackpantech.leitner_system_automaton.domain.sessions.SessionsRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Domain service to study flashcards and move them up or down boxes in the repository
 */
public class LeitnerSystemService {

    private final FlashcardsRepository flashcardsRepository;

    private final BoxesRepository boxesRepository;

    private final SessionsRepository sessionsRepository;

    public LeitnerSystemService(final FlashcardsRepository flashcardsRepository,
                                final BoxesRepository boxesRepository,
                                final SessionsRepository sessionsRepository) {
        this.flashcardsRepository = flashcardsRepository;
        this.boxesRepository = boxesRepository;
        this.sessionsRepository = sessionsRepository;
    }

    /**
     * Gets session flashcards
     *
     * @return session flashcards
     */
    public List<Flashcard> getSessionQuestionnaire() {
        final List<Box> boxes = boxesRepository.getAllBoxes();
        final int numberOfSessions = sessionsRepository.getIncrementedNumberOfSessions();

        final List<Box> sessionBoxes = getSessionBoxes(numberOfSessions, boxes);

        return getFlashcardsFromBoxes(sessionBoxes);
    }

    /**
     * Gets session flashcards with a given tag
     *
     * @param tag tag to filter session flashcards
     *
     * @return session flashcards with tag
     */
    public List<Flashcard> getSessionQuestionnaireWithTag(final String tag) {
        final List<Box> boxes = boxesRepository.getAllBoxes();
        final int numberOfSessions = sessionsRepository.getIncrementedNumberOfSessions();

        final List<Box> sessionBoxes = getSessionBoxes(numberOfSessions, boxes);

        return getFlashcardsWithTagFromBoxes(sessionBoxes, tag);
    }

    /**
     * Gets session's boxes with the number of sessions since the beginning of the study plan
     *
     * @param numberOfSessions number of sessions since the beginning of the study plan
     * @param boxes boxes to filter
     *
     * @return session's boxes
     */
    private List<Box> getSessionBoxes(final int numberOfSessions, final List<Box> boxes) {
        return boxes.stream().filter(box -> numberOfSessions % box.frequency() == 0).toList();
    }

    /**
     * Gets flashcards from given boxes
     *
     * @param boxes boxes to get flashcards from
     *
     * @return flashcards in given boxes
     */
    private List<Flashcard> getFlashcardsFromBoxes(final List<Box> boxes) {
        List<Flashcard> sessionFlashcards = new ArrayList<>();

        boxes.forEach(box -> {
            try {
                sessionFlashcards.addAll(flashcardsRepository.getAllFlashcardsFromBox(box.id()));
            } catch (BoxNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        return sessionFlashcards;
    }

    /**
     * Gets flashcards with given tag from given boxes
     *
     * @param boxes boxes to get flashcards from
     * @param tag tag to filter session flashcards
     *
     * @return flashcards with given tag in given boxes
     */
    private List<Flashcard> getFlashcardsWithTagFromBoxes(final List<Box> boxes, final String tag) {
        List<Flashcard> sessionFlashcards = new ArrayList<>();

        boxes.forEach(box ->
        {
            try {
                sessionFlashcards.addAll(flashcardsRepository.getAllFlashcardsWithTagFromBox(box.id(), tag));
            } catch (BoxNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        return sessionFlashcards;
    }

    /**
     * Evaluates answered flashcard.
     * If it was correctly answered, the flashcard will be moved to the next box or deleted if it was already in the last one.
     * Else it will be moved backed to the first box.
     *
     * @param flashcardId flashcard ID to evaluate
     * @param isCorrect evaluation of given flashcard
     *
     * @throws BoxNotFoundException if next box was not found
     * @throws FlashcardNotFoundException if flashcard was not found
     */
    public void evaluateFlashcard(final long flashcardId, final boolean isCorrect)
            throws FlashcardNotFoundException, BoxNotFoundException {
        final Flashcard flashcard = flashcardsRepository.getFlashcard(flashcardId);

        final long nextBoxId = getNextBoxId(isCorrect, flashcard.currentBox().id());

        moveOrDeleteFlashcard(nextBoxId, flashcard);
    }

    /**
     * Increments or decrements box ID depending on the value of isCorrect
     *
     * @param isCorrect true to increment, false to decrement
     * @param boxId box ID to update
     *
     * @return incremented box ID if isCorrect is true, else first box ID
     */
    private long getNextBoxId(final boolean isCorrect, final long boxId) {
        return isCorrect ? boxId + 1L : boxesRepository.getFirstBoxId();
    }

    /**
     * Moves flashcard to the next box or deletes it if it was in the last box
     *
     * @param nextBoxId next box ID
     * @param flashcard flashcard to move or delete
     *
     * @throws BoxNotFoundException if next box was not found
     * @throws FlashcardNotFoundException if flashcard was not found
     */
    private void moveOrDeleteFlashcard(final long nextBoxId, final Flashcard flashcard)
            throws BoxNotFoundException, FlashcardNotFoundException {
        if (nextBoxId <= boxesRepository.getLastBoxId()) {
            final Box nextBox = boxesRepository.getBox(nextBoxId);
            flashcardsRepository.editFlashcard(flashcard.id(), flashcard.question(), flashcard.answer(), flashcard.tags(), nextBox);
        } else {
            flashcardsRepository.deleteFlashcard(flashcard.id());
        }
    }

}
