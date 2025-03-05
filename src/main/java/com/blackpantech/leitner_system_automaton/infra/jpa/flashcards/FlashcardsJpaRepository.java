package com.blackpantech.leitner_system_automaton.infra.jpa.flashcards;

import com.blackpantech.leitner_system_automaton.infra.jpa.boxes.BoxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FlashcardsJpaRepository extends JpaRepository<FlashcardEntity, Long> {

    @Query("select flashcards from FlashcardEntity flashcards where :tag in flashcards.tags")
    List<FlashcardEntity> findAllWithTag(final String tag);

    List<FlashcardEntity> findAllByBox(final BoxEntity box);

    @Query("select flashcards from FlashcardEntity flashcards where :box = flashcards.currentBox and :tag in flashcards.tags")
    List<FlashcardEntity> findAllWithTagInBox(final String tag, final BoxEntity box);

}
