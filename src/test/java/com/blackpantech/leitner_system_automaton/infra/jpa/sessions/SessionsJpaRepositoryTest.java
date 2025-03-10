package com.blackpantech.leitner_system_automaton.infra.jpa.sessions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class SessionsJpaRepositoryTest {

    @Autowired
    SessionsJpaRepository sessionsJpaRepository;

    @Test
    @DisplayName("should increment the current number of sessions")
    void shouldIncrementNumberOfSessions() {
        final SessionEntity currentSession = sessionsJpaRepository.findFirstByOrderById();
        final int currentNumberOfSessions = currentSession.getSessionNumber();
        currentSession.incrementSessionNumber();
        sessionsJpaRepository.save(currentSession);
        final int incrementedNumberOfSessions = sessionsJpaRepository.findFirstByOrderById().getSessionNumber();

        assertEquals(currentNumberOfSessions + 1, incrementedNumberOfSessions);
    }

}
