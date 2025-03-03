package com.blackpantech.leitner_system_automaton.infra.jpa.boxes;

import com.blackpantech.leitner_system_automaton.domain.boxes.Box;
import com.blackpantech.leitner_system_automaton.domain.boxes.BoxesRepository;
import com.blackpantech.leitner_system_automaton.domain.boxes.exceptions.BoxNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA implementation of the boxes repository
 */
@Repository
public class JpaBoxesRepository implements BoxesRepository {

    private final BoxesJpaRepository boxesJpaRepository;

    private final BoxEntityMapper boxEntityMapper;

    public JpaBoxesRepository(final BoxesJpaRepository boxesJpaRepository, final BoxEntityMapper boxEntityMapper) {
        this.boxesJpaRepository = boxesJpaRepository;
        this.boxEntityMapper = boxEntityMapper;
    }

    @Override
    public Box getBox(long id) throws BoxNotFoundException {
        return null;
    }

    @Override
    public List<Box> getAllBoxes() {
        return List.of();
    }

}
