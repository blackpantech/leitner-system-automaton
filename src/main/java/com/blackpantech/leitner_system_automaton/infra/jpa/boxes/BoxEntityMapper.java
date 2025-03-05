package com.blackpantech.leitner_system_automaton.infra.jpa.boxes;

import com.blackpantech.leitner_system_automaton.domain.boxes.Box;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapstruct mapper JPA Box Object -> Domain Box Object
 */
@Mapper(componentModel = "spring")
public interface BoxEntityMapper {

    /**
     * Mapper JPA Box Object -> Domain Box Object
     *
     * @param boxEntity JPA Box Object
     *
     * @return Domain Box Object
     */
    Box BoxEntityToBox(final BoxEntity boxEntity);

    /**
     * Mapper Domain Box Object -> JPA Box Object
     *
     * @param box Domain Box Object
     *
     * @return JPA Box Object
     */
    BoxEntity BoxToBoxEntity(final Box box);

    /**
     * Mapper list of JPA Box Objects -> list of Domain Box Objects
     *
     * @param boxEntities list of JPA Box Objects
     *
     * @return list of Domain Box Objects
     */
    List<Box> BoxEntitiesToBoxes(List<BoxEntity> boxEntities);

}
