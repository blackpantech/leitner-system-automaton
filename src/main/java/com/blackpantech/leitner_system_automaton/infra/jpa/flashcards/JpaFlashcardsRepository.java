package com.blackpantech.leitner_system_automaton.infra.jpa.flashcards;

import com.blackpantech.leitner_system_automaton.domain.boxes.Box;
import com.blackpantech.leitner_system_automaton.domain.flashcards.Flashcard;
import com.blackpantech.leitner_system_automaton.domain.flashcards.FlashcardsRepository;
import com.blackpantech.leitner_system_automaton.domain.flashcards.exceptions.FlashcardNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA implementation of the flashcards repository
 */
@Repository
public class JpaFlashcardsRepository implements FlashcardsRepository {

    private final FlashcardsJpaRepository flashcardsJpaRepository;

    private final FlashcardEntityMapper flashcardEntityMapper;

    public JpaFlashcardsRepository(final FlashcardsJpaRepository flashcardsJpaRepository, final FlashcardEntityMapper flashcardEntityMapper) {
        this.flashcardsJpaRepository = flashcardsJpaRepository;
        this.flashcardEntityMapper = flashcardEntityMapper;
    }

    @Override
    public Flashcard createFlashcard(String question, String answer, String[] tags) {
        return null;
    }

    @Override
    public Flashcard editFlashcard(long id, String question, String answer, String[] tags, Box currentBox) throws FlashcardNotFoundException {
        return null;
    }

    @Override
    public Flashcard getFlashcard(long id) throws FlashcardNotFoundException {
        return null;
    }

    @Override
    public void deleteFlashcard(long id) throws FlashcardNotFoundException {

    }

    @Override
    public List<Flashcard> getAllFlashcards() {
        return List.of();
    }

    @Override
    public List<Flashcard> getAllFlashcardsWithTag(String tag) {
        return List.of();
    }

    @Override
    public List<Flashcard> getAllFlashcardsFromBox(long boxId) {
        return List.of();
    }

    @Override
    public List<Flashcard> getAllFlashcardsWithTagFromBox(long boxId, String tag) {
        return List.of();
    }

}
