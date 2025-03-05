package com.blackpantech.leitner_system_automaton.infra.jpa.flashcards;

import com.blackpantech.leitner_system_automaton.domain.boxes.Box;
import com.blackpantech.leitner_system_automaton.domain.boxes.exceptions.BoxNotFoundException;
import com.blackpantech.leitner_system_automaton.domain.flashcards.Flashcard;
import com.blackpantech.leitner_system_automaton.domain.flashcards.exceptions.FlashcardNotFoundException;
import com.blackpantech.leitner_system_automaton.infra.jpa.boxes.BoxEntity;
import com.blackpantech.leitner_system_automaton.infra.jpa.boxes.BoxesJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest
public class JpaFlashcardsRepositoryTest {

    @MockitoBean
    FlashcardsJpaRepository flashcardsJpaRepository;

    @MockitoBean
    BoxesJpaRepository boxesJpaRepository;

    @Autowired
    JpaFlashcardsRepository jpaFlashcardsRepository;

    final BoxEntity box1 = new BoxEntity(1);

    final Box dummyBox = new Box(0L, 1);

    @ParameterizedTest
    @CsvSource({
            "What is the capital of France?, Paris, Geography",
            "What is the return code range for server errors?, 500, Programming"
    })
    @DisplayName("should create new flashcard")
    void shouldCreateFlashcard(final String question, final String answer, final String tags) {
        final FlashcardEntity expectedFlashcard = new FlashcardEntity(question, answer, new String[]{tags}, box1);
        when(flashcardsJpaRepository.save(expectedFlashcard)).thenReturn(expectedFlashcard);
        when(boxesJpaRepository.findFirstByOrderByFrequency()).thenReturn(box1);

        final Flashcard createdFlashcard = jpaFlashcardsRepository.createFlashcard(question, answer, new String[]{tags});

        assertNotNull(createdFlashcard);
        verify(boxesJpaRepository).findFirstByOrderByFrequency();
        verify(flashcardsJpaRepository).save(expectedFlashcard);
        verifyNoMoreInteractions(boxesJpaRepository, flashcardsJpaRepository);
    }

    @ParameterizedTest
    @CsvSource({
            "10, What is the capital of France?, Paris, Geography",
            "45, What is the return code range for server errors?, 500, Programming"
    })
    @DisplayName("should edit an existing flashcard")
    void shouldEditFlashcard(final long id, final String question, final String answer, final String tags)
            throws FlashcardNotFoundException {
        final FlashcardEntity expectedFlashcard = new FlashcardEntity(question, answer, new String[]{tags}, box1);
        when(flashcardsJpaRepository.findById(id)).thenReturn(Optional.of(expectedFlashcard));
        final FlashcardEntity editedFlashcard = new FlashcardEntity(question + "edit",
                answer + "edit",
                new String[]{tags, "edit"},
                box1);
        when(flashcardsJpaRepository.save(editedFlashcard)).thenReturn(editedFlashcard);

        final Flashcard returnedEditedFlashcard = jpaFlashcardsRepository.editFlashcard(id,
                question + "edit",
                answer + "edit",
                new String[]{tags, "edit"},
                dummyBox);

        assertNotNull(returnedEditedFlashcard);
        verify(flashcardsJpaRepository).findById(id);
        verify(flashcardsJpaRepository).save(expectedFlashcard);
        verifyNoMoreInteractions(flashcardsJpaRepository);
    }

    @ParameterizedTest
    @CsvSource({
            "10, What is the capital of France?, Paris, Geography",
            "45, What is the return code range for server errors?, 500, Programming"
    })
    @DisplayName("should throw not found when editing a flashcard")
    void shouldThrowFlashcardNotFoundException_whenEditFlashcard(final long id,
                                                                 final String question,
                                                                 final String answer,
                                                                 final String tags) {
        when(flashcardsJpaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(FlashcardNotFoundException.class,
                () -> jpaFlashcardsRepository.editFlashcard(id, question, answer, new String[]{tags}, dummyBox));

        verify(flashcardsJpaRepository).findById(id);
        verifyNoMoreInteractions(flashcardsJpaRepository);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 4L, 10L})
    @DisplayName("should get an existing flashcard")
    void shouldGetFlashcard(final long id) throws FlashcardNotFoundException {
        final FlashcardEntity dummyFlashcard = new FlashcardEntity("", "", new String[]{}, box1);
        when(flashcardsJpaRepository.findById(id)).thenReturn(Optional.of(dummyFlashcard));

        final Flashcard returnedFlashcard = jpaFlashcardsRepository.getFlashcard(id);

        assertNotNull(returnedFlashcard);
        verify(flashcardsJpaRepository).findById(id);
        verifyNoMoreInteractions(flashcardsJpaRepository);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 4L, 10L})
    @DisplayName("should throw not found when getting a flashcard")
    void shouldThrowFlashcardNotFoundException_whenGetFlashcard(final long id) {
        when(flashcardsJpaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(FlashcardNotFoundException.class, () -> jpaFlashcardsRepository.getFlashcard(id));

        verify(flashcardsJpaRepository).findById(id);
        verifyNoMoreInteractions(flashcardsJpaRepository);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 4L, 10L})
    @DisplayName("should delete an existing flashcard")
    void shouldDeleteFlashcard(final long id) throws FlashcardNotFoundException {
        final FlashcardEntity flashcardToDelete = new FlashcardEntity("", "", new String[]{}, box1);
        when(flashcardsJpaRepository.findById(id)).thenReturn(Optional.of(flashcardToDelete));

        jpaFlashcardsRepository.deleteFlashcard(id);

        verify(flashcardsJpaRepository).findById(id);
        verify(flashcardsJpaRepository).delete(flashcardToDelete);
        verifyNoMoreInteractions(flashcardsJpaRepository);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 4L, 10L})
    @DisplayName("should throw not found when deleting a flashcard")
    void shouldThrowFlashcardNotFoundException_whenDeleteFlashcard(final long id) {
        when(flashcardsJpaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(FlashcardNotFoundException.class, () -> jpaFlashcardsRepository.deleteFlashcard(id));

        verify(flashcardsJpaRepository).findById(id);
        verifyNoMoreInteractions(flashcardsJpaRepository);
    }

    @Test
    @DisplayName("should get all flashcards")
    void shouldGetAllFlashcards() {
        final FlashcardEntity dummyFlashcard = new FlashcardEntity("", "", new String[]{}, box1);
        final List<FlashcardEntity> expectedFlashcards = Collections.singletonList(dummyFlashcard);
        when(flashcardsJpaRepository.findAll()).thenReturn(expectedFlashcards);

        final List<Flashcard> flashcards = jpaFlashcardsRepository.getAllFlashcards();

        assertNotNull(flashcards);
        verify(flashcardsJpaRepository).findAll();
        verifyNoMoreInteractions(flashcardsJpaRepository);
    }

    @ParameterizedTest
    @ValueSource(strings = {"history", "maths", "Java"})
    @DisplayName("should get all flashcards with tag")
    void shouldGetAllFlashcardsWithTag(final String tag) {
        final FlashcardEntity dummyFlashcard = new FlashcardEntity("", "", new String[]{tag}, box1);
        final List<FlashcardEntity> expectedFlashcards = Collections.singletonList(dummyFlashcard);
        when(flashcardsJpaRepository.findAllWithTag(tag)).thenReturn(expectedFlashcards);

        final List<Flashcard> flashcards = jpaFlashcardsRepository.getAllFlashcardsWithTag(tag);

        assertNotNull(flashcards);
        verify(flashcardsJpaRepository).findAllWithTag(tag);
        verifyNoMoreInteractions(flashcardsJpaRepository);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 4L, 6L})
    @DisplayName("should get all flashcards from box")
    void shouldGetAllFlashcardsFromBox(final long boxId) throws BoxNotFoundException {
        final BoxEntity box = new BoxEntity(1 << (int) boxId);
        final FlashcardEntity dummyFlashcard = new FlashcardEntity("", "", new String[]{}, box);
        final List<FlashcardEntity> expectedFlashcards = Collections.singletonList(dummyFlashcard);
        when(boxesJpaRepository.findById(boxId)).thenReturn(Optional.of(box));
        when(flashcardsJpaRepository.findAllByBox(box)).thenReturn(expectedFlashcards);

        final List<Flashcard> flashcards = jpaFlashcardsRepository.getAllFlashcardsFromBox(boxId);

        assertNotNull(flashcards);
        verify(boxesJpaRepository).findById(boxId);
        verify(flashcardsJpaRepository).findAllByBox(box);
        verifyNoMoreInteractions(flashcardsJpaRepository, boxesJpaRepository);
    }

    @ParameterizedTest
    @CsvSource({
            "1, Geography",
            "4, Programming"
    })
    @DisplayName("should get all flashcards with tag from box")
    void shouldGetAllFlashcardsWithTagFromBox(final long boxId, final String tag) throws BoxNotFoundException {
        final BoxEntity box = new BoxEntity(1 << (int) boxId);
        final FlashcardEntity dummyFlashcard = new FlashcardEntity("", "", new String[]{tag}, box);
        final List<FlashcardEntity> expectedFlashcards = Collections.singletonList(dummyFlashcard);
        when(boxesJpaRepository.findById(boxId)).thenReturn(Optional.of(box));
        when(flashcardsJpaRepository.findAllWithTagInBox(tag, box)).thenReturn(expectedFlashcards);

        final List<Flashcard> flashcards = jpaFlashcardsRepository.getAllFlashcardsWithTagFromBox(boxId, tag);

        assertNotNull(flashcards);
        verify(boxesJpaRepository).findById(boxId);
        verify(flashcardsJpaRepository).findAllWithTagInBox(tag, box);
        verifyNoMoreInteractions(flashcardsJpaRepository, boxesJpaRepository);
    }

}
