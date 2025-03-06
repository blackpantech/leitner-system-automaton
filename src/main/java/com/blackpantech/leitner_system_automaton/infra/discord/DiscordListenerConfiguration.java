package com.blackpantech.leitner_system_automaton.infra.discord;

import com.blackpantech.leitner_system_automaton.domain.flashcards.FlashcardsService;
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
    public JDA jda(@Value("${discord.token}") final String token, final FlashcardsService flashcardsService) {
        final JDA jda = JDABuilder.createLight(token, Collections.emptyList())
                .addEventListeners(new SlashCommandsListener(flashcardsService))
                .build();

        // Register your commands to make them visible globally on Discord:
        final CommandListUpdateAction commands = jda.updateCommands();

        // Add all your commands on this action instance
        final CommandListUpdateAction commandData =  commands.addCommands(
                Commands.slash("create-flashcard", "creates a new flashcard")
                        .addOption(STRING, "question", "Question of the flashcard", true)
                        .addOption(STRING, "answer", "Answer of the question", true)
                        .addOption(STRING, "tags", "Tags, separate with commas"),
                Commands.slash("edit-flashcard", "edits an existing flashcard")
                        .addOption(INTEGER, "id", "ID of the flashcard", true)
                        .addOption(STRING, "question", "Question of the flashcard", true)
                        .addOption(STRING, "answer", "Answer of the question", true)
                        .addOption(STRING, "tags", "Tags, separate with commas"),
                Commands.slash("get-flashcard", "gets an existing flashcard")
                        .addOption(INTEGER, "id", "ID of the flashcard", true),
                Commands.slash("delete-flashcard", "deletes an existing flashcard")
                        .addOption(INTEGER, "id", "ID of the flashcard", true),
                Commands.slash("get-all-flashcards", "gets all flashcards"),
                Commands.slash("get-all-flashcards-with-tag", "gets all flashcards with tag")
                        .addOption(STRING, "tag", "Tag to filter flashcards", true)
        );

        // Then finally send your commands to discord using the API
        commandData.queue();

        return jda;
    }

}
