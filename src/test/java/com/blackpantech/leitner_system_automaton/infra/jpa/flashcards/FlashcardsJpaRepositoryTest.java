package com.blackpantech.leitner_system_automaton.infra.jpa.flashcards;

import com.blackpantech.leitner_system_automaton.infra.jpa.boxes.BoxEntity;
import com.blackpantech.leitner_system_automaton.infra.jpa.boxes.BoxesJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FlashcardsJpaRepositoryTest {

    @Autowired
    BoxesJpaRepository boxesJpaRepository;

    @Autowired
    FlashcardsJpaRepository flashcardsJpaRepository;

    @ParameterizedTest
    @CsvSource({
            "Geography, 2",
            "Programming, 1",
            "Java, 1",
            "Maths, 1",
            "Literature, 0"
    })
    @DisplayName("should find all with tag")
    void shouldFindAllWithTag(final String tag, final int expectedNumberOfElements) {
        final List<FlashcardEntity> flashcardsWithTag = flashcardsJpaRepository.findAllWithTag(tag);

        assertEquals(expectedNumberOfElements, flashcardsWithTag.size());
    }

    @ParameterizedTest
    @CsvSource({
            "3, 2",
            "1, 1",
            "5, 1",
            "7, 1",
            "6, 0"
    })
    @DisplayName("should find all by current box")
    void shouldFindAllByCurrentBox(final long boxId, final int expectedNumberOfElements) {
        final Optional<BoxEntity> box = boxesJpaRepository.findById(boxId);
        if (box.isEmpty()) {
            fail();
        }
        final List<FlashcardEntity> flashcardsFromBox = flashcardsJpaRepository.findAllByCurrentBox(box.get());

        assertEquals(expectedNumberOfElements, flashcardsFromBox.size());
    }

    @ParameterizedTest
    @CsvSource({
            "Geography, 1, 1",
            "Geography, 7, 1",
            "Maths, 5, 1",
            "Programming, 1, 0",
    })
    @DisplayName("should find all flashcards with tag by current box")
    void shouldFindAllByCurrentBox(final String tag, final long boxId, final int expectedNumberOfElements) {
        final Optional<BoxEntity> box = boxesJpaRepository.findById(boxId);
        if (box.isEmpty()) {
            fail();
        }
        final List<FlashcardEntity> flashcardsFromBox = flashcardsJpaRepository.findAllWithTagInBox(tag, box.get());

        assertEquals(expectedNumberOfElements, flashcardsFromBox.size());
    }

}
