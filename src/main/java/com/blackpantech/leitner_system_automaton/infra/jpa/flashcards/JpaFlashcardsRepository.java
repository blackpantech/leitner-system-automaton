package com.blackpantech.leitner_system_automaton.infra.jpa.flashcards;

import com.blackpantech.leitner_system_automaton.domain.boxes.Box;
import com.blackpantech.leitner_system_automaton.domain.boxes.exceptions.BoxNotFoundException;
import com.blackpantech.leitner_system_automaton.domain.flashcards.Flashcard;
import com.blackpantech.leitner_system_automaton.domain.flashcards.FlashcardsRepository;
import com.blackpantech.leitner_system_automaton.domain.flashcards.exceptions.FlashcardNotFoundException;
import com.blackpantech.leitner_system_automaton.infra.jpa.boxes.BoxEntity;
import com.blackpantech.leitner_system_automaton.infra.jpa.boxes.BoxEntityMapper;
import com.blackpantech.leitner_system_automaton.infra.jpa.boxes.BoxesJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * JPA implementation of the flashcards repository
 */
@Repository
public class JpaFlashcardsRepository implements FlashcardsRepository {

    private final FlashcardsJpaRepository flashcardsJpaRepository;

    private final BoxesJpaRepository boxesJpaRepository;

    private final FlashcardEntityMapper flashcardEntityMapper;

    private final BoxEntityMapper boxEntityMapper;

    public JpaFlashcardsRepository(final FlashcardsJpaRepository flashcardsJpaRepository,
                                   final BoxesJpaRepository boxesJpaRepository,
                                   final FlashcardEntityMapper flashcardEntityMapper,
                                   final BoxEntityMapper boxEntityMapper) {
        this.flashcardsJpaRepository = flashcardsJpaRepository;
        this.boxesJpaRepository = boxesJpaRepository;
        this.flashcardEntityMapper = flashcardEntityMapper;
        this.boxEntityMapper = boxEntityMapper;
    }

    @Override
    public Flashcard createFlashcard(final String question, final String answer, final String[] tags) {
        final BoxEntity startingBox = boxesJpaRepository.findFirstByOrderByFrequency();

        final FlashcardEntity flashcardToCreate = new FlashcardEntity(question, answer, tags, startingBox);

        final FlashcardEntity createdFlashcard = flashcardsJpaRepository.save(flashcardToCreate);

        return flashcardEntityMapper.FlashcardEntityToFlashcard(createdFlashcard);
    }

    @Override
    public Flashcard editFlashcard(final long id,
                                   final String question,
                                   final String answer,
                                   final String[] tags,
                                   final Box currentBox)
            throws FlashcardNotFoundException {
        final FlashcardEntity flashcardToEdit = getNonOptionalFlashcard(id);

        final FlashcardEntity editedFlashcard = getEditedFlashcard(flashcardToEdit, question, answer, tags, currentBox);

        final FlashcardEntity savedEditedFlashcard = flashcardsJpaRepository.save(editedFlashcard);

        return flashcardEntityMapper.FlashcardEntityToFlashcard(savedEditedFlashcard);
    }

    @Override
    public Flashcard editFlashcard(final long id,
                                   final String question,
                                   final String answer,
                                   final String[] tags)
            throws FlashcardNotFoundException {
        final FlashcardEntity flashcardToEdit = getNonOptionalFlashcard(id);

        final FlashcardEntity editedFlashcard = getEditedFlashcard(flashcardToEdit, question, answer, tags);

        final FlashcardEntity savedEditedFlashcard = flashcardsJpaRepository.save(editedFlashcard);

        return flashcardEntityMapper.FlashcardEntityToFlashcard(savedEditedFlashcard);
    }

    /**
     * Edits flashcard object with new fields
     *
     * @param flashcardToEdit flashcard to edit
     * @param question edited question
     * @param answer edited answer
     * @param tags edited tags
     * @param currentBox edited box
     *
     * @return edited flashcard
     */
    private FlashcardEntity getEditedFlashcard(final FlashcardEntity flashcardToEdit,
                                               final String question,
                                               final String answer,
                                               final String[] tags,
                                               final Box currentBox) {
        flashcardToEdit.setQuestion(question);
        flashcardToEdit.setAnswer(answer);
        flashcardToEdit.setTags(tags);
        flashcardToEdit.setCurrentBox(boxEntityMapper.BoxToBoxEntity(currentBox));

        return flashcardToEdit;
    }

    /**
     * Edits flashcard object with new fields
     *
     * @param flashcardToEdit flashcard to edit
     * @param question edited question
     * @param answer edited answer
     * @param tags edited tags
     *
     * @return edited flashcard
     */
    private FlashcardEntity getEditedFlashcard(final FlashcardEntity flashcardToEdit,
                                               final String question,
                                               final String answer,
                                               final String[] tags) {
        flashcardToEdit.setQuestion(question);
        flashcardToEdit.setAnswer(answer);
        flashcardToEdit.setTags(tags);

        return flashcardToEdit;
    }

    /**
     * Gets potentially non-existing flashcard
     *
     * @param id ID of the flashcard to get
     *
     * @return found flashcard with given ID
     *
     * @throws FlashcardNotFoundException if no flashcard was found with given ID
     */
    private FlashcardEntity getNonOptionalFlashcard(final long id) throws FlashcardNotFoundException{
        final Optional<FlashcardEntity> optionalFlashcard = flashcardsJpaRepository.findById(id);

        if (optionalFlashcard.isEmpty()) {
            throw new FlashcardNotFoundException(id);
        }

        return optionalFlashcard.get();
    }

    @Override
    public Flashcard getFlashcard(final long id) throws FlashcardNotFoundException {
        final FlashcardEntity foundFlashcard = getNonOptionalFlashcard(id);

        return flashcardEntityMapper.FlashcardEntityToFlashcard(foundFlashcard);
    }

    @Override
    public void deleteFlashcard(final long id) throws FlashcardNotFoundException {
        final FlashcardEntity flashcardToDelete = getNonOptionalFlashcard(id);

        flashcardsJpaRepository.delete(flashcardToDelete);
    }

    @Override
    public List<Flashcard> getAllFlashcards() {
        return flashcardEntityMapper.FlashcardEntitiesToFlashcards(flashcardsJpaRepository.findAll());
    }

    @Override
    public List<Flashcard> getAllFlashcardsWithTag(final String tag) {
        return flashcardEntityMapper.FlashcardEntitiesToFlashcards(flashcardsJpaRepository.findAllWithTag(tag));
    }

    @Override
    public List<Flashcard> getAllFlashcardsFromBox(final long boxId) throws BoxNotFoundException {
        final BoxEntity boxToFetch = getNonOptionalBox(boxId);

        return flashcardEntityMapper.FlashcardEntitiesToFlashcards(flashcardsJpaRepository.findAllByCurrentBox(boxToFetch));
    }

    @Override
    public List<Flashcard> getAllFlashcardsWithTagFromBox(final long boxId, final String tag)
            throws BoxNotFoundException {
        final BoxEntity boxToFetch = getNonOptionalBox(boxId);

        return flashcardEntityMapper.FlashcardEntitiesToFlashcards(
                flashcardsJpaRepository.findAllWithTagInBox(tag, boxToFetch)
        );
    }

    /**
     * Gets potentially non-existing box
     *
     * @param id ID of the box to get
     *
     * @return found box with given ID
     *
     * @throws BoxNotFoundException if no box was found with given ID
     */
    private BoxEntity getNonOptionalBox(final long id) throws BoxNotFoundException {
        final Optional<BoxEntity> optionalBox = boxesJpaRepository.findById(id);

        if (optionalBox.isEmpty()) {
            throw new BoxNotFoundException(id);
        }

        return optionalBox.get();
    }

}
