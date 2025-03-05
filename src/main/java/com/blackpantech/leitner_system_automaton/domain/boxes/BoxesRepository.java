package com.blackpantech.leitner_system_automaton.domain.boxes;

import com.blackpantech.leitner_system_automaton.domain.boxes.exceptions.BoxNotFoundException;

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

    /**
     * Gets first box ID
     *
     * @return first box ID
     */
    long getFirstBoxId();

    /**
     * Gets last box ID
     *
     * @return last box ID
     */
    long getLastBoxId();

}
