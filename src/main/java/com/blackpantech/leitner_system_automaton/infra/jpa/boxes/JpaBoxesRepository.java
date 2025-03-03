package com.blackpantech.leitner_system_automaton.infra.jpa.boxes;

import com.blackpantech.leitner_system_automaton.domain.boxes.Box;
import com.blackpantech.leitner_system_automaton.domain.boxes.BoxesRepository;
import com.blackpantech.leitner_system_automaton.domain.boxes.exceptions.BoxNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
    public Box getBox(final long id) throws BoxNotFoundException {
        final Optional<BoxEntity> optionalBox = boxesJpaRepository.findById(id);

        final BoxEntity box = getBoxFromOptionalBox(id, optionalBox);

        return boxEntityMapper.BoxEntityToBox(box);
    }

    private BoxEntity getBoxFromOptionalBox(final long id, final Optional<BoxEntity> optionalBox)
            throws BoxNotFoundException {
        if (optionalBox.isEmpty()) {
            throw new BoxNotFoundException(id);
        }

        return optionalBox.get();
    }

    @Override
    public List<Box> getAllBoxes() {
        final List<BoxEntity> boxes = boxesJpaRepository.findAll();

        return boxEntityMapper.BoxEntitiesToBoxes(boxes);
    }

}
