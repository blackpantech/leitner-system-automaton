package com.blackpantech.leitner_system_automaton.domain.sessions;

/**
 * Sessions repository interface
 */
public interface SessionsRepository {

    /**
     * Gets the incremented number of sessions
     *
     * @return incremented number of sessions
     */
    int getIncrementedNumberOfSessions();

}
