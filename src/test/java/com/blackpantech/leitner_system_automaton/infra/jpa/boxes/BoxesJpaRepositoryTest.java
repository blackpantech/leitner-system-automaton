package com.blackpantech.leitner_system_automaton.infra.jpa.boxes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class BoxesJpaRepositoryTest {

    @Autowired
    BoxesJpaRepository boxesJpaRepository;

    final List<BoxEntity> boxes = Arrays.asList(
            new BoxEntity(1),
            new BoxEntity(2),
            new BoxEntity(4),
            new BoxEntity(8),
            new BoxEntity(16),
            new BoxEntity(32),
            new BoxEntity(64)
    );

    @Test
    @DisplayName("should find the first box with smallest frequency")
    void shouldFindFirstByOrderByFrequency() {
        boxesJpaRepository.saveAll(boxes);

        final BoxEntity firstBox = boxesJpaRepository.findFirstByOrderByFrequency();

        assertEquals(boxes.getFirst(), firstBox);
    }

}
