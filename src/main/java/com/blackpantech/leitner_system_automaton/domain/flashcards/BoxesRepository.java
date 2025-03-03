package com.blackpantech.leitner_system_automaton.domain.flashcards;

import com.blackpantech.leitner_system_automaton.domain.flashcards.exceptions.BoxNotFoundException;

import java.util.List;

/**
 * Boxes repository interface
 */
public interface BoxesRepository {

    /**
     * Gets a box with an ID
     *
     * @param id box ID
     *
     * @return box with given ID
     *
     * @throws BoxNotFoundException if no box has given ID
     */
    Box getBox(final long id) throws BoxNotFoundException;

    /**
     * Gets all boxes
     *
     * @return all boxes
     */
    List<Box> getAllBoxes();

}
