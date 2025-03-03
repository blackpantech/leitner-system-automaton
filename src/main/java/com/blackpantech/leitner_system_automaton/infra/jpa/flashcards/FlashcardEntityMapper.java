package com.blackpantech.leitner_system_automaton.infra.jpa.flashcards;

import com.blackpantech.leitner_system_automaton.domain.flashcards.Flashcard;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapstruct mapper JPA Flashcard Object -> Domain Flashcard Object
 */
@Mapper(componentModel = "spring")
public interface FlashcardEntityMapper {

    /**
     * Mapper JPA Flashcard Object -> Domain Flashcard Object
     *
     * @param flashcardEntity JPA Flashcard Object
     *
     * @return Domain Flashcard Object
     */
    Flashcard FlashcardEntityToFlashcard(final FlashcardEntity flashcardEntity);

    /**
     * Mapper list of JPA Flashcard Objects -> list of Domain Flashcard Objects
     *
     * @param flashcardEntities list of JPA Flashcard Objects
     *
     * @return list of Domain Flashcard Objects
     */
    List<Flashcard> FlashcardEntitiesToFlashcards(List<FlashcardEntity> flashcardEntities);

}
