package com.blackpantech.leitner_system_automaton.infra.jpa.boxes;

import com.blackpantech.leitner_system_automaton.domain.boxes.Box;
import com.blackpantech.leitner_system_automaton.domain.boxes.exceptions.BoxNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest
public class JpaBoxesRepositoryTest {

    @MockitoBean
    BoxesJpaRepository boxesJpaRepository;

    @Autowired
    JpaBoxesRepository jpaBoxesRepository;

    final List<BoxEntity> boxes = Arrays.asList(
            new BoxEntity(0L, 1),
            new BoxEntity(1L, 2),
            new BoxEntity(2L, 4),
            new BoxEntity(3L, 8),
            new BoxEntity(4L, 16),
            new BoxEntity(5L, 32),
            new BoxEntity(6L, 64)
    );

    @ParameterizedTest
    @ValueSource(longs = {0, 1, 2, 3, 4, 5, 6})
    @DisplayName("should get box with ID")
    void shouldGetBox(final long id) throws BoxNotFoundException {
        when(boxesJpaRepository.findById(id)).thenReturn(Optional.of(boxes.get((int) id)));

        final Box box = jpaBoxesRepository.getBox(id);

        assertEquals(boxes.get((int) id).getFrequency(), box.frequency());
        verify(boxesJpaRepository).findById(id);
        verifyNoMoreInteractions(boxesJpaRepository);
    }

    @ParameterizedTest
    @ValueSource(longs = {7, 10, 25})
    @DisplayName("should not find box when getting box with ID")
    void shouldNotFindBox_whenGetBox(final long id) {
        when(boxesJpaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(BoxNotFoundException.class, () -> jpaBoxesRepository.getBox(id));

        verify(boxesJpaRepository).findById(id);
        verifyNoMoreInteractions(boxesJpaRepository);
    }

    @Test
    @DisplayName("should get all boxes")
    void shouldGetAllBoxes() {
        when(boxesJpaRepository.findAll()).thenReturn(boxes);

        final List<Box> returnedBoxes = jpaBoxesRepository.getAllBoxes();

        verify(boxesJpaRepository).findAll();
        verifyNoMoreInteractions(boxesJpaRepository);
        assertEquals(boxes.size(), returnedBoxes.size());
    }

}
