package com.blackpantech.leitner_system_automaton.domain.flashcards;

import com.blackpantech.leitner_system_automaton.domain.flashcards.exceptions.FlashcardNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class FlashcardsServiceTest {

    @Mock
    final FlashcardsRepository flashcardsRepository = mock(FlashcardsRepository.class);

    final FlashcardsService flashcardsService = new FlashcardsService(flashcardsRepository);

    final Box dummyBox = new Box(1L, 1);

    @ParameterizedTest
    @CsvSource({
            "What is the capital of France?, Paris, Geography",
            "What is the return code range for server errors?, 500, Programmation"
    })
    @DisplayName("should create new flashcard")
    void shouldCreateFlashcard(final String question, final String answer, final String tags) {
        final Flashcard expectedFlashcard = new Flashcard(0L, question, answer, new String[]{tags}, dummyBox);
        when(flashcardsRepository.createFlashcard(question, answer, new String[]{tags})).thenReturn(expectedFlashcard);

        final Flashcard createdFlashcard = flashcardsService.createFlashcard(question, answer, new String[]{tags});

        assertEquals(expectedFlashcard, createdFlashcard);
        verify(flashcardsRepository).createFlashcard(question, answer, new String[]{tags});
        verifyNoMoreInteractions(flashcardsRepository);
    }

    @ParameterizedTest
    @CsvSource({
            "10, What is the capital of France?, Paris, Geography",
            "45, What is the return code range for server errors?, 500, Programmation"
    })
    @DisplayName("should edit an existing flashcard")
    void shouldEditFlashcard(final long id, final String question, final String answer, final String tags)
            throws FlashcardNotFoundException {
        final Flashcard expectedFlashcard = new Flashcard(id, question, answer, new String[]{tags}, dummyBox);
        when(flashcardsRepository.editFlashcard(id, question, answer, new String[]{tags}))
                .thenReturn(expectedFlashcard);

        final Flashcard editedFlashcard = flashcardsService.editFlashcard(id, question, answer, new String[]{tags});

        assertEquals(expectedFlashcard, editedFlashcard);
        verify(flashcardsRepository).editFlashcard(id, question, answer, new String[]{tags});
        verifyNoMoreInteractions(flashcardsRepository);
    }

    @ParameterizedTest
    @CsvSource({
            "10, What is the capital of France?, Paris, Geography",
            "45, What is the return code range for server errors?, 500, Programmation"
    })
    @DisplayName("should throw not found when editing a flashcard")
    void shouldThrowFlashcardNotFoundException_whenEditFlashcard(final long id,
                                                                 final String question,
                                                                 final String answer,
                                                                 final String tags)
            throws FlashcardNotFoundException {
        doThrow(new FlashcardNotFoundException(id))
                .when(flashcardsRepository).editFlashcard(id, question, answer, new String[]{tags});

        assertThrows(FlashcardNotFoundException.class,
                () -> flashcardsService.editFlashcard(id, question, answer, new String[]{tags}));

        verify(flashcardsRepository).editFlashcard(id, question, answer, new String[]{tags});
        verifyNoMoreInteractions(flashcardsRepository);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 4L, 10L})
    @DisplayName("should get an existing flashcard")
    void shouldGetFlashcard(final long id) throws FlashcardNotFoundException {
        final Flashcard dummyFlashcard = new Flashcard(id, "", "", new String[]{}, dummyBox);
        when(flashcardsRepository.getFlashcard(id)).thenReturn(dummyFlashcard);

        final Flashcard returnedFlashcard = flashcardsService.getFlashcard(id);

        assertEquals(dummyFlashcard, returnedFlashcard);
        verify(flashcardsRepository).getFlashcard(id);
        verifyNoMoreInteractions(flashcardsRepository);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 4L, 10L})
    @DisplayName("should throw not found when getting a flashcard")
    void shouldThrowFlashcardNotFoundException_whenGetFlashcard(final long id) throws FlashcardNotFoundException {
        doThrow(new FlashcardNotFoundException(id)).when(flashcardsRepository).getFlashcard(id);

        assertThrows(FlashcardNotFoundException.class, () -> flashcardsService.getFlashcard(id));

        verify(flashcardsRepository).getFlashcard(id);
        verifyNoMoreInteractions(flashcardsRepository);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 4L, 10L})
    @DisplayName("should delete an existing flashcard")
    void shouldDeleteFlashcard(final long id) throws FlashcardNotFoundException {
        flashcardsService.deleteFlashcard(id);

        verify(flashcardsRepository).deleteFlashcard(id);
        verifyNoMoreInteractions(flashcardsRepository);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 4L, 10L})
    @DisplayName("should throw not found when deleting a flashcard")
    void shouldThrowFlashcardNotFoundException_whenDeleteFlashcard(final long id) throws FlashcardNotFoundException {
        doThrow(new FlashcardNotFoundException(id)).when(flashcardsRepository).deleteFlashcard(id);

        assertThrows(FlashcardNotFoundException.class, () -> flashcardsService.deleteFlashcard(id));

        verify(flashcardsRepository).deleteFlashcard(id);
        verifyNoMoreInteractions(flashcardsRepository);
    }

    @Test
    @DisplayName("should get all flashcards")
    void shouldGetAllFlashcards() {
        final Flashcard dummyFlashcard = new Flashcard(1L, "", "", new String[]{}, dummyBox);
        final List<Flashcard> expectedFlashcards = Collections.singletonList(dummyFlashcard);
        when(flashcardsRepository.getAllFlashcards()).thenReturn(expectedFlashcards);

        final List<Flashcard> flashcards = flashcardsService.getAllFlashcards();

        assertNotNull(flashcards);
        verify(flashcardsRepository).getAllFlashcards();
        verifyNoMoreInteractions(flashcardsRepository);
    }

    @ParameterizedTest
    @ValueSource(strings = {"history", "maths", "Java"})
    @DisplayName("should get all flashcards with tag")
    void shouldGetAllFlashcardsWithTag(final String tag) {
        final Flashcard dummyFlashcard = new Flashcard(1L, "", "", new String[]{ tag }, dummyBox);
        final List<Flashcard> expectedFlashcards = Collections.singletonList(dummyFlashcard);
        when(flashcardsRepository.getAllFlashcardsWithTag(tag)).thenReturn(expectedFlashcards);

        final List<Flashcard> flashcardsWithTag = flashcardsService.getAllFlashcardsWithTag(tag);

        assertNotNull(flashcardsWithTag);
        verify(flashcardsRepository).getAllFlashcardsWithTag(tag);
        verifyNoMoreInteractions(flashcardsRepository);
    }

}
