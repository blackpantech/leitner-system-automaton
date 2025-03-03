package com.blackpantech.leitner_system_automaton.domain.leitner_system;

import com.blackpantech.leitner_system_automaton.domain.flashcards.Box;
import com.blackpantech.leitner_system_automaton.domain.flashcards.BoxesRepository;
import com.blackpantech.leitner_system_automaton.domain.flashcards.Flashcard;
import com.blackpantech.leitner_system_automaton.domain.flashcards.FlashcardsRepository;
import com.blackpantech.leitner_system_automaton.domain.flashcards.exceptions.BoxNotFoundException;
import com.blackpantech.leitner_system_automaton.domain.flashcards.exceptions.FlashcardNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class LeitnerSystemServiceTest {

    @Mock
    final FlashcardsRepository flashcardsRepository = mock(FlashcardsRepository.class);

    @Mock
    final BoxesRepository boxesRepository = mock(BoxesRepository.class);

    final LeitnerSystemService leitnerSystemService = new LeitnerSystemService(flashcardsRepository, boxesRepository);

    final List<Box> boxes = Arrays.asList(
            new Box(0L, 1),
            new Box(1L, 2),
            new Box(2L, 4),
            new Box(3L, 8),
            new Box(4L, 16),
            new Box(5L, 32),
            new Box(6L, 64)
    );

    long[] convertCharArrayToLongArray(final char[] chars) {
        long[] longs = new long[chars.length];

        for (int i = 0 ; i < chars.length ; i++) {
            longs[i] = Character.getNumericValue(chars[i]);
        }

        return longs;
    }

    @ParameterizedTest
    @CsvSource({
            "1, '0'",
            "2, '01'",
            "4, '012'",
            "5, '0'",
            "8, '0123'",
            "16, '01234'",
            "32, '012345'",
            "64, '0123456'",
            "96, '012345'"
    })
    @DisplayName("should get daily questionnaire")
    void shouldGetDailyQuestionnaire(final int daysSinceBeginning, final String boxesIdString) {
        when(boxesRepository.getAllBoxes()).thenReturn(boxes);
        final long[] boxesId = convertCharArrayToLongArray(boxesIdString.toCharArray());
        for (long boxId : boxesId) {
            when(flashcardsRepository.getAllFlashcardsFromBox(boxId)).thenReturn(
                    Collections.singletonList(
                            new Flashcard(0L, "", "", new String[]{}, boxes.get((int) boxId))
                    )
            );
        }

        final List<Flashcard> dailyQuestionnaire = leitnerSystemService.getDailyQuestionnaire(daysSinceBeginning);

        assertEquals(boxesId.length, dailyQuestionnaire.size());
        verify(boxesRepository).getAllBoxes();
        verify(flashcardsRepository, times(boxesId.length)).getAllFlashcardsFromBox(anyLong());
        verifyNoMoreInteractions(boxesRepository, flashcardsRepository);
    }

    @ParameterizedTest
    @CsvSource({
            "1, Geography, '0'",
            "16, Programming, '01234'"
    })
    @DisplayName("should get daily questionnaire with specific tag")
    void shouldGetDailyQuestionnaireWithTag(final int daysSinceBeginning, final String tag, final String boxesIdString) {
        when(boxesRepository.getAllBoxes()).thenReturn(boxes);
        final long[] boxesId = convertCharArrayToLongArray(boxesIdString.toCharArray());
        for (long boxId : boxesId) {
            when(flashcardsRepository.getAllFlashcardsWithTagFromBox(boxId, tag)).thenReturn(
                    Collections.singletonList(
                            new Flashcard(0L, "", "", new String[]{tag}, boxes.get((int) boxId))
                    )
            );
        }

        List<Flashcard> dailyQuestionnaire = leitnerSystemService.getDailyQuestionnaireWithTag(daysSinceBeginning, tag);

        assertEquals(boxesId.length, dailyQuestionnaire.size());
        verify(boxesRepository).getAllBoxes();
        verify(flashcardsRepository, times(boxesId.length)).getAllFlashcardsWithTagFromBox(anyLong(), eq(tag));
        verifyNoMoreInteractions(boxesRepository, flashcardsRepository);
    }

    @ParameterizedTest
    @CsvSource({
            "1, true, 2",
            "1, false, 2",
            "1, true, 6"
    })
    @DisplayName("should evaluate flashcard")
    void shouldEvaluateFlashCard(final long id, final boolean isCorrect, final int boxIndex)
            throws FlashcardNotFoundException, BoxNotFoundException {
        final Flashcard flashcard = new Flashcard(id, "", "", new String[] {}, boxes.get(boxIndex));
        when(flashcardsRepository.getFlashcard(id)).thenReturn(flashcard);
        if (boxIndex >= boxes.size() - 1) {
            leitnerSystemService.evaluateFlashcard(id, isCorrect);

            verify(flashcardsRepository).deleteFlashcard(id);
        } else {
            final Box nextBox = isCorrect ? boxes.get(boxIndex + 1) : boxes.getFirst();
            when(boxesRepository.getBox(nextBox.id())).thenReturn(nextBox);
             final Flashcard editedFlashcard = new Flashcard(id, "", "", new String[] {}, nextBox);
            when(flashcardsRepository.editFlashcard(id, "", "", new String[] {}, nextBox))
                .thenReturn(editedFlashcard);

            leitnerSystemService.evaluateFlashcard(id, isCorrect);

            verify(boxesRepository).getBox(nextBox.id());
            verify(flashcardsRepository).editFlashcard(id, "", "", new String[] {}, nextBox);
        }

        verify(flashcardsRepository).getFlashcard(id);
        verifyNoMoreInteractions(flashcardsRepository, boxesRepository);
    }

}
