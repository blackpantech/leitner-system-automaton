package com.blackpantech.leitner_system_automaton.infra.discord;

import com.blackpantech.leitner_system_automaton.domain.flashcards.Flashcard;
import com.blackpantech.leitner_system_automaton.domain.flashcards.FlashcardsService;
import com.blackpantech.leitner_system_automaton.domain.flashcards.exceptions.FlashcardNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Objects;

@Controller
public class SlashCommandsListener extends ListenerAdapter {

    private final FlashcardsService flashcardsService;

    private final static String ID = "id";
    private final static String QUESTION = "question";
    private final static String ANSWER = "answer";
    private final static String TAGS = "tags";
    private final static String TAG = "tag";

    public SlashCommandsListener(FlashcardsService flashcardsService) {
        this.flashcardsService = flashcardsService;
    }

    @Override
    public void onSlashCommandInteraction(final SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "create-flashcard" -> createFlashcard(event);
            case "edit-flashcard" -> {
                try {
                    editFlashcard(event);
                } catch (FlashcardNotFoundException e) {
                    event.reply(e.getMessage()).queue();
                }
            }
            case "get-flashcard" -> {
                try {
                    getFlashcard(event);
                } catch (FlashcardNotFoundException e) {
                    event.reply(e.getMessage()).queue();

                }
            }
            case "delete-flashcard" -> {
                try {
                    deleteFlashcard(event);
                } catch (FlashcardNotFoundException e) {
                    event.reply(e.getMessage()).queue();
                }
            }
            case "get-all-flashcards" -> getAllFlashcards(event);
            case "get-all-flashcards-with-tag" -> getAllFlashcardsWithTag(event);
            default -> {
            }
        }
    }

    private void createFlashcard(final SlashCommandInteractionEvent event) {
        final String question = event.getOption(QUESTION, OptionMapping::getAsString);
        final String answer = event.getOption(ANSWER, OptionMapping::getAsString);
        final String[] tags = getTagsFromRawText(event);

        final Flashcard createdFlashcard = flashcardsService.createFlashcard(question, answer, tags);

        final String createdFlashcardJson = writeObjectAsJson(createdFlashcard);

        event.reply(createdFlashcardJson).queue();
    }

    private void editFlashcard(final SlashCommandInteractionEvent event) throws FlashcardNotFoundException {
        final long id = Objects.requireNonNull(event.getOption(ID, OptionMapping::getAsInt)).longValue();
        final String question = event.getOption(QUESTION, OptionMapping::getAsString);
        final String answer = event.getOption(ANSWER, OptionMapping::getAsString);
        final String[] tags = getTagsFromRawText(event);

        final Flashcard editedFlashcard = flashcardsService.editFlashcard(id, question, answer, tags);

        final String editedFlashcardJson = writeObjectAsJson(editedFlashcard);

        event.reply(editedFlashcardJson).queue();
    }

    private void getFlashcard(final SlashCommandInteractionEvent event) throws FlashcardNotFoundException {
        final long id = Objects.requireNonNull(event.getOption(ID, OptionMapping::getAsInt)).longValue();

        final Flashcard foundFlashcard = flashcardsService.getFlashcard(id);

        final String foundFlashcardJson = writeObjectAsJson(foundFlashcard);

        event.reply(foundFlashcardJson).queue();
    }

    private void deleteFlashcard(final SlashCommandInteractionEvent event) throws FlashcardNotFoundException {
        final long id = Objects.requireNonNull(event.getOption(ID, OptionMapping::getAsInt)).longValue();

        flashcardsService.deleteFlashcard(id);

        event.reply("Flashcard deleted").queue();
    }

    private void getAllFlashcards(final SlashCommandInteractionEvent event) {
        final List<Flashcard> foundFlashcards = flashcardsService.getAllFlashcards();

        final String foundFlashcardsJson = writeObjectAsJson(foundFlashcards);

        event.reply(foundFlashcardsJson).queue();
    }

    private void getAllFlashcardsWithTag(final SlashCommandInteractionEvent event) {
        final String tag = event.getOption(TAG, OptionMapping::getAsString);

        final List<Flashcard> foundFlashcardsWithTag = flashcardsService.getAllFlashcardsWithTag(tag);

        final String foundFlashcardsWithTagJson = writeObjectAsJson(foundFlashcardsWithTag);

        event.reply(foundFlashcardsWithTagJson).queue();
    }

    private String[] getTagsFromRawText(final SlashCommandInteractionEvent event) {
        final String rawTags = event.getOption(TAGS, OptionMapping::getAsString);

        return rawTags == null ? new String[]{} : rawTags.split(",");
    }

    private String writeObjectAsJson(final Object object) {
        final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
