package com.blackpantech.leitner_system_automaton.infra.jpa.boxes;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BoxesJpaRepository extends JpaRepository<BoxEntity, Long> {

    BoxEntity findFirstByOrderByFrequency();

}
