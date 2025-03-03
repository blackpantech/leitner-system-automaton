package com.blackpantech.leitner_system_automaton.infra.jpa.flashcards;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FlashcardsJpaRepository extends JpaRepository<FlashcardEntity, Long> {
}
