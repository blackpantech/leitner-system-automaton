package com.blackpantech.leitner_system_automaton.infra.jpa.sessions;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionsJpaRepository extends JpaRepository<SessionEntity, Integer> {

    SessionEntity findFirstByOrderById();

}
