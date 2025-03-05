package com.blackpantech.leitner_system_automaton.infra.jpa.flashcards;

import com.blackpantech.leitner_system_automaton.infra.jpa.boxes.BoxEntity;
import com.blackpantech.leitner_system_automaton.infra.jpa.boxes.BoxesJpaRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FlashcardsJpaRepositoryTest {

    @Autowired
    BoxesJpaRepository boxesJpaRepository;

    @Autowired
    FlashcardsJpaRepository flashcardsJpaRepository;

    final List<BoxEntity> boxes = Arrays.asList(
            new BoxEntity(1),
            new BoxEntity(2),
            new BoxEntity(4),
            new BoxEntity(8),
            new BoxEntity(16),
            new BoxEntity(32),
            new BoxEntity(64)
    );

    final List<FlashcardEntity> flashcards = Arrays.asList(
            new FlashcardEntity("question 1", "answer 1", new String[]{"Geography"}, boxes.getFirst()),
            new FlashcardEntity("question 2", "answer 2", new String[]{"Programming"}, boxes.get(2)),
            new FlashcardEntity("question 3", "answer 3", new String[]{"Java"}, boxes.get(2)),
            new FlashcardEntity("question 4", "answer 4", new String[]{"Maths"}, boxes.get(4)),
            new FlashcardEntity("question 5", "answer 5", new String[]{"Geography"}, boxes.getLast())
    );

    @BeforeAll
    void init() {
        boxesJpaRepository.saveAll(boxes);
        flashcardsJpaRepository.saveAll(flashcards);
    }

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
            "2, 2",
            "0, 1",
            "4, 1",
            "6, 1",
            "5, 0"
    })
    @DisplayName("should find all by current box")
    void shouldFindAllByCurrentBox(final int boxIndex, final int expectedNumberOfElements) {
        final List<FlashcardEntity> flashcardsFromBox = flashcardsJpaRepository.findAllByCurrentBox(boxes.get(boxIndex));

        assertEquals(expectedNumberOfElements, flashcardsFromBox.size());
    }

    @ParameterizedTest
    @CsvSource({
            "Geography, 0, 1",
            "Geography, 6, 1",
            "Maths, 4, 1",
            "Programming, 0, 0",
    })
    @DisplayName("should find all flashcards with tag by current box")
    void shouldFindAllByCurrentBox(final String tag, final int boxIndex, final int expectedNumberOfElements) {
        final List<FlashcardEntity> flashcardsFromBox = flashcardsJpaRepository.findAllWithTagInBox(tag, boxes.get(boxIndex));

        assertEquals(expectedNumberOfElements, flashcardsFromBox.size());
    }

}
