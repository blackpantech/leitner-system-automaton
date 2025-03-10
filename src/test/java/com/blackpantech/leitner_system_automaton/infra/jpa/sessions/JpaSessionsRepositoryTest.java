package com.blackpantech.leitner_system_automaton.infra.jpa.sessions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest
public class JpaSessionsRepositoryTest {

    @MockitoBean
    SessionsJpaRepository sessionsJpaRepository;

    @Autowired
    JpaSessionsRepository jpaSessionsRepository;

    @Test
    @DisplayName("should get the incremented number of sessions")
    void shouldGetIncrementedNumberOfSessions() {
        final SessionEntity session = new SessionEntity(0);
        final SessionEntity incrementedSession = new SessionEntity(1);
        when(sessionsJpaRepository.findFirstByOrderById()).thenReturn(session);
        when(sessionsJpaRepository.save(incrementedSession)).thenReturn(incrementedSession);

        final int numberOfSessions = jpaSessionsRepository.getIncrementedNumberOfSessions();

        assertEquals(incrementedSession.getSessionNumber(), numberOfSessions);
        verify(sessionsJpaRepository).findFirstByOrderById();
        verify(sessionsJpaRepository).save(incrementedSession);
        verifyNoMoreInteractions(sessionsJpaRepository);
    }

}
