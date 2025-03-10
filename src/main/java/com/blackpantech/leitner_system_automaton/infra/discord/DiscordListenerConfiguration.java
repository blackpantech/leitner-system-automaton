package com.blackpantech.leitner_system_automaton.infra.discord;

import com.blackpantech.leitner_system_automaton.domain.flashcards.FlashcardsService;
import com.blackpantech.leitner_system_automaton.domain.leitner_system.LeitnerSystemService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

import static net.dv8tion.jda.api.interactions.commands.OptionType.INTEGER;
import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

@Configuration
public class DiscordListenerConfiguration {

    @Bean
    public JDA jda(@Value("${discord.token}") final String token,
                   final FlashcardsService flashcardsService,
                   final LeitnerSystemService leitnerSystemService) {
        final JDA jda = JDABuilder.createLight(token, Collections.emptyList())
                .addEventListeners(new SlashCommandsListener(flashcardsService, leitnerSystemService))
                .build();

        // Registering commands to make them visible globally on Discord:
        final CommandListUpdateAction commands = jda.updateCommands();

        // Adding all commands on this action instance
        final CommandListUpdateAction commandData =  commands.addCommands(
                Commands.slash(DiscordConstants.CREATE_FLASHCARD_EVENT, "Creates a new flashcard")
                        .addOption(STRING, DiscordConstants.QUESTION, "Question of the flashcard", true)
                        .addOption(STRING, DiscordConstants.ANSWER, "Answer of the question", true)
                        .addOption(STRING, DiscordConstants.TAGS, "Tags, separate with commas"),
                Commands.slash(DiscordConstants.EDIT_FLASHCARD_EVENT, "Edits an existing flashcard")
                        .addOption(INTEGER, DiscordConstants.ID, "ID of the flashcard", true)
                        .addOption(STRING, DiscordConstants.QUESTION, "Question of the flashcard", true)
                        .addOption(STRING, DiscordConstants.ANSWER, "Answer of the question", true)
                        .addOption(STRING, DiscordConstants.TAGS, "Tags, separate with commas"),
                Commands.slash(DiscordConstants.GET_FLASHCARD_EVENT, "Gets an existing flashcard")
                        .addOption(INTEGER, DiscordConstants.ID, "ID of the flashcard", true),
                Commands.slash(DiscordConstants.DELETE_FLASHCARD_EVENT, "Deletes an existing flashcard")
                        .addOption(INTEGER, DiscordConstants.ID, "ID of the flashcard", true),
                Commands.slash(DiscordConstants.GET_ALL_FLASHCARDS_EVENT, "Gets all flashcards"),
                Commands.slash(DiscordConstants.GET_ALL_FLASHCARDS_WITH_TAG_EVENT, "Gets all flashcards with tag")
                        .addOption(STRING, DiscordConstants.TAG, "Tag to filter flashcards", true),
                Commands.slash(DiscordConstants.GET_SESSION_QUESTIONNAIRE_EVENT, "Get a new questionnaire"),
                Commands.slash(DiscordConstants.GET_SESSION_QUESTIONNAIRE_WITH_TAG_EVENT, "Get a new questionnaire with a specific tag")
                        .addOption(STRING, DiscordConstants.TAG, "Tag to filter flashcards", true)
        );

        // Then finally sending commands to discord using the API
        commandData.queue();

        return jda;
    }

}
