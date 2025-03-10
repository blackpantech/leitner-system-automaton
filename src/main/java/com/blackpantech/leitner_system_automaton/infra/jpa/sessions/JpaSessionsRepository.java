package com.blackpantech.leitner_system_automaton.infra.jpa.sessions;

import com.blackpantech.leitner_system_automaton.domain.sessions.SessionsRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA implementation of the sessions repository
 */
@Repository
public class JpaSessionsRepository implements SessionsRepository {

    private final SessionsJpaRepository sessionsJpaRepository;

    public JpaSessionsRepository(final SessionsJpaRepository sessionsJpaRepository) {
        this.sessionsJpaRepository = sessionsJpaRepository;
    }

    @Override
    public int getIncrementedNumberOfSessions() {
        final SessionEntity currentSession = sessionsJpaRepository.findFirstByOrderById();

        final SessionEntity incrementedSession = incrementsSession(currentSession);

        final SessionEntity savedIncrementedSession = sessionsJpaRepository.save(incrementedSession);

        return savedIncrementedSession.getSessionNumber();
    }

    /**
     * Increments the number of session in SessionEntity object
     *
     * @param session session entity to increment
     *
     * @return session entity with incremented number of sessions
     */
    private SessionEntity incrementsSession(final SessionEntity session) {
        session.incrementSessionNumber();

        return session;
    }

}
