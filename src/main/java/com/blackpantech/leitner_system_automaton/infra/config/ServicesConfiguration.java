package com.blackpantech.leitner_system_automaton.infra.config;

import com.blackpantech.leitner_system_automaton.domain.boxes.BoxesRepository;
import com.blackpantech.leitner_system_automaton.domain.flashcards.FlashcardsRepository;
import com.blackpantech.leitner_system_automaton.domain.flashcards.FlashcardsService;
import com.blackpantech.leitner_system_automaton.domain.leitner_system.LeitnerSystemService;
import com.blackpantech.leitner_system_automaton.domain.sessions.SessionsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for bean factories
 */
@Configuration
public class ServicesConfiguration {

    /**
     * Bean factory for FlashcardsService
     *
     * @param flashcardsRepository flashcards repository
     *
     * @return flashcards service bean
     */
    @Bean
    public FlashcardsService flashcardsService(final FlashcardsRepository flashcardsRepository) {
        return new FlashcardsService(flashcardsRepository);
    }

    /**
     * Bean factory for LeitnerSystemService
     *
     * @param flashcardsRepository flashcards repository
     * @param boxesRepository boxes repository
     *
     * @return Leitner system service bean
     */
    @Bean
    public LeitnerSystemService leitnerSystemService(final FlashcardsRepository flashcardsRepository,
                                                     final BoxesRepository boxesRepository,
                                                     final SessionsRepository sessionsRepository) {
        return new LeitnerSystemService(flashcardsRepository, boxesRepository, sessionsRepository);
    }

}
