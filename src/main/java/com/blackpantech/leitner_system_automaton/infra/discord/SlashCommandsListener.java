package com.blackpantech.leitner_system_automaton.infra.discord;

import com.blackpantech.leitner_system_automaton.domain.boxes.exceptions.BoxNotFoundException;
import com.blackpantech.leitner_system_automaton.domain.flashcards.Flashcard;
import com.blackpantech.leitner_system_automaton.domain.flashcards.FlashcardsService;
import com.blackpantech.leitner_system_automaton.domain.flashcards.exceptions.FlashcardNotFoundException;
import com.blackpantech.leitner_system_automaton.domain.leitner_system.LeitnerSystemService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Objects;

@Controller
public class SlashCommandsListener extends ListenerAdapter {

    private final FlashcardsService flashcardsService;

    private final LeitnerSystemService leitnerSystemService;

    public SlashCommandsListener(final FlashcardsService flashcardsService,
                                 final LeitnerSystemService leitnerSystemService) {
        this.flashcardsService = flashcardsService;
        this.leitnerSystemService = leitnerSystemService;
    }

    /**
     * Associates application actions with slash commands event names
     *
     * @param slashCommandEvent slash command event
     */
    @Override
    public void onSlashCommandInteraction(final SlashCommandInteractionEvent slashCommandEvent) {
        switch (slashCommandEvent.getName()) {
            case DiscordConstants.CREATE_FLASHCARD_EVENT -> createFlashcard(slashCommandEvent);
            case DiscordConstants.EDIT_FLASHCARD_EVENT -> {
                try {
                    editFlashcard(slashCommandEvent);
                } catch (FlashcardNotFoundException e) {
                    replyMessage(slashCommandEvent, e.getMessage());
                }
            }
            case DiscordConstants.GET_FLASHCARD_EVENT -> {
                try {
                    getFlashcard(slashCommandEvent);
                } catch (FlashcardNotFoundException e) {
                    replyMessage(slashCommandEvent, e.getMessage());

                }
            }
            case DiscordConstants.DELETE_FLASHCARD_EVENT -> {
                try {
                    deleteFlashcard(slashCommandEvent);
                } catch (FlashcardNotFoundException e) {
                    replyMessage(slashCommandEvent, e.getMessage());
                }
            }
            case DiscordConstants.GET_ALL_FLASHCARDS_EVENT -> getAllFlashcards(slashCommandEvent);
            case DiscordConstants.GET_ALL_FLASHCARDS_WITH_TAG_EVENT -> getAllFlashcardsWithTag(slashCommandEvent);
            case DiscordConstants.GET_SESSION_QUESTIONNAIRE_EVENT -> getSessionQuestionnaire(slashCommandEvent);
            case DiscordConstants.GET_SESSION_QUESTIONNAIRE_WITH_TAG_EVENT ->
                    getSessionQuestionnaireWithTag(slashCommandEvent);
            default -> {
            }
        }
    }

    /**
     * Creates a flashcard
     *
     * @param slashCommandEvent slash command event
     */
    private void createFlashcard(final SlashCommandInteractionEvent slashCommandEvent) {
        final String question = slashCommandEvent.getOption(DiscordConstants.QUESTION, OptionMapping::getAsString);
        final String answer = slashCommandEvent.getOption(DiscordConstants.ANSWER, OptionMapping::getAsString);
        final String[] tags = getTagsFromRawText(slashCommandEvent);

        final Flashcard createdFlashcard = flashcardsService.createFlashcard(question, answer, tags);

        final String createdFlashcardJson = writeObjectAsJson(createdFlashcard);

        replyMessage(slashCommandEvent, createdFlashcardJson);
    }

    /**
     * Edits a flashcard with given ID
     *
     * @param slashCommandEvent slash command event
     *
     * @throws FlashcardNotFoundException – if no flashcard has given ID
     */
    private void editFlashcard(final SlashCommandInteractionEvent slashCommandEvent) throws FlashcardNotFoundException {
        final long id = getNumericValueFromField(slashCommandEvent, DiscordConstants.ID);
        final String question = slashCommandEvent.getOption(DiscordConstants.QUESTION, OptionMapping::getAsString);
        final String answer = slashCommandEvent.getOption(DiscordConstants.ANSWER, OptionMapping::getAsString);
        final String[] tags = getTagsFromRawText(slashCommandEvent);

        final Flashcard editedFlashcard = flashcardsService.editFlashcard(id, question, answer, tags);

        final String editedFlashcardJson = writeObjectAsJson(editedFlashcard);

        replyMessage(slashCommandEvent, editedFlashcardJson);
    }

    /**
     * Gets a flashcard with given ID
     *
     * @param slashCommandEvent slash command event
     *
     * @throws FlashcardNotFoundException if no flashcard has given ID
     */
    private void getFlashcard(final SlashCommandInteractionEvent slashCommandEvent) throws FlashcardNotFoundException {
        final long id = getNumericValueFromField(slashCommandEvent, DiscordConstants.ID);

        final Flashcard foundFlashcard = flashcardsService.getFlashcard(id);

        final String foundFlashcardJson = writeObjectAsJson(foundFlashcard);

        replyMessage(slashCommandEvent, foundFlashcardJson);
    }

    /**
     * Deletes a  flashcard with given ID
     *
     * @param slashCommandEvent slash command event
     *
     * @throws FlashcardNotFoundException if no flashcard has given ID
     */
    private void deleteFlashcard(final SlashCommandInteractionEvent slashCommandEvent)
            throws FlashcardNotFoundException {
        final long id = getNumericValueFromField(slashCommandEvent, DiscordConstants.ID);

        flashcardsService.deleteFlashcard(id);

        replyMessage(slashCommandEvent, "Flashcard deleted");
    }

    /**
     * Gets a list of all flashcards
     *
     * @param slashCommandEvent slash command event
     */
    private void getAllFlashcards(final SlashCommandInteractionEvent slashCommandEvent) {
        final List<Flashcard> foundFlashcards = flashcardsService.getAllFlashcards();

        final String foundFlashcardsJson = writeObjectAsJson(foundFlashcards);

        replyMessage(slashCommandEvent, foundFlashcardsJson);
    }

    /**
     * Gets a list of all flashcards with a given tag
     *
     * @param slashCommandEvent slash command event
     */
    private void getAllFlashcardsWithTag(final SlashCommandInteractionEvent slashCommandEvent) {
        final String tag = slashCommandEvent.getOption(DiscordConstants.TAG, OptionMapping::getAsString);

        final List<Flashcard> foundFlashcardsWithTag = flashcardsService.getAllFlashcardsWithTag(tag);

        final String foundFlashcardsWithTagJson = writeObjectAsJson(foundFlashcardsWithTag);

        replyMessage(slashCommandEvent, foundFlashcardsWithTagJson);
    }

    /**
     * Gets session flashcards
     *
     * @param slashCommandEvent slash command event
     */
    private void getSessionQuestionnaire(final SlashCommandInteractionEvent slashCommandEvent) {
        final int sessionNumber = getNumericValueFromField(slashCommandEvent, DiscordConstants.SESSION);

        final List<Flashcard> sessionFlashcards = leitnerSystemService.getSessionQuestionnaire(sessionNumber);

        sendSessionFlashcards(slashCommandEvent, sessionFlashcards);
    }

    /**
     * Gets session flashcards with a given tag
     *
     * @param slashCommandEvent slash command event
     */
    private void getSessionQuestionnaireWithTag(final SlashCommandInteractionEvent slashCommandEvent) {
        final int sessionNumber = getNumericValueFromField(slashCommandEvent, DiscordConstants.SESSION);
        final String tag = slashCommandEvent.getOption(DiscordConstants.TAG, OptionMapping::getAsString);

        final List<Flashcard> sessionFlashcardsWithTag =
                leitnerSystemService.getSessionQuestionnaireWithTag(sessionNumber, tag);

        sendSessionFlashcards(slashCommandEvent, sessionFlashcardsWithTag);
    }

    /**
     * Gets a numeric value from a given field name
     *
     * @param slashCommandEvent slash command event
     * @param fieldName field name
     *
     * @return numeric value contained in the field
     */
    private int getNumericValueFromField(final SlashCommandInteractionEvent slashCommandEvent, final String fieldName) {
        return Objects.requireNonNull(slashCommandEvent.getOption(fieldName, OptionMapping::getAsInt));
    }

    /**
     * Gets an array of tags from a raw text with tags separated by commas
     *
     * @param slashCommandEvent slash command event
     *
     * @return array of tags
     */
    private String[] getTagsFromRawText(final SlashCommandInteractionEvent slashCommandEvent) {
        final String rawTags = slashCommandEvent.getOption(DiscordConstants.TAGS, OptionMapping::getAsString);

        return rawTags == null ? new String[]{} : rawTags.split(",");
    }

    /**
     * Writes a Java object as a JSON value
     *
     * @param object object to write as JSON
     *
     * @return object as JSON value
     */
    private String writeObjectAsJson(final Object object) {
        final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Replies to a slash command message
     *
     * @param slashCommandEvent slash command event to reply to
     * @param message reply message
     */
    private void replyMessage(final SlashCommandInteractionEvent slashCommandEvent, final String message) {
        slashCommandEvent.reply(message).queue();
    }

    /**
     * Replies to a button message
     * @param buttonEvent button event to reply to
     * @param message reply message
     */
    private void replyMessage(final ButtonInteractionEvent buttonEvent, final String message) {
        buttonEvent.reply(message).queue();
    }

    /**
     * Sends session's flashcards to the channel
     *
     * @param slashCommandEvent slash command event
     * @param sessionFlashcards session's flashcards to send
     */
    private void sendSessionFlashcards(final SlashCommandInteractionEvent slashCommandEvent,
                                       final List<Flashcard> sessionFlashcards) {
        // Lets the user know we received the command before doing anything else
        slashCommandEvent.deferReply(false).queue();

        for (final Flashcard sessionFlashcard : sessionFlashcards) {
            final String positiveButtonId = createButtonId(DiscordConstants.CORRECT, sessionFlashcard.id());
            final String negativeButtonId = createButtonId(DiscordConstants.INCORRECT, sessionFlashcard.id());
            final String flashcardMessage = createFlashcardMessage(sessionFlashcard);

            sendDetachedMessageWithButtons(slashCommandEvent, flashcardMessage, positiveButtonId, negativeButtonId);
        }
    }

    /**
     * Creates a formatted button ID with a generic command name and a unique button attribute
     *
     * @param genericCommandName generic command name
     * @param uniqueButtonAttribute unique button attribute
     *
     * @return formatted button ID with <unique button attribute>:<generic command name>
     */
    private String createButtonId(final String genericCommandName, final long uniqueButtonAttribute) {
        return String.format("%d:%s", uniqueButtonAttribute, genericCommandName);
    }

    /**
     * Creates a message from a flashcard
     *
     * @param flashcard given flashcard
     *
     * @return formatted message made from the given flashcard
     */
    private String createFlashcardMessage(final Flashcard flashcard) {
        final String spoilerAnswer = createSpoilerText(flashcard.answer());
        return flashcard.question() + " " + spoilerAnswer;
    }

    /**
     * Creates a formatted text to appear as spoiler on Discord
     *
     * @param text text to flag as spoiler
     *
     * @return formatted text with spoiler flag
     */
    private String createSpoilerText(final String text) {
        return String.format("||%s||", text);
    }

    /**
     * Sends a message with 2 buttons to the channel without having to reply to another message
     *
     * @param slashCommandEvent slash command event
     * @param message message to send
     * @param positiveButtonId ID for the positive button
     * @param negativeButtonId ID for the negative button
     */
    private void sendDetachedMessageWithButtons(final SlashCommandInteractionEvent slashCommandEvent,
                                                final String message,
                                                final String positiveButtonId,
                                                final String negativeButtonId) {
        slashCommandEvent.getHook()
                .sendMessage(message)
                .addActionRow(
                        Button.primary(positiveButtonId, DiscordConstants.POSITIVE_CHECKMARK),
                        Button.danger(negativeButtonId, DiscordConstants.NEGATIVE_CHECKMARK)
                )
                .queue();
    }

    /**
     * Listener for buttons interactions
     *
     * @param buttonEvent button event
     */
    @Override
    public void onButtonInteraction(final ButtonInteractionEvent buttonEvent) {
        // acknowledge the button was clicked, otherwise the interaction will fail
        buttonEvent.deferEdit().queue();
        // deletes the message with the question and buttons to clean the channel
        buttonEvent.getMessage().delete().queue();
        // processes the interaction with buttons
        processButtonInteraction(buttonEvent);
    }

    /**
     * Processes interactions with buttons
     *
     * @param buttonEvent button event
     */
    private void processButtonInteraction(final ButtonInteractionEvent buttonEvent) {
        // this is the custom id we specified in our button
        final String[] splitButtonId = buttonEvent.getComponentId().split(":");
        final long flashcardId = Long.parseLong(splitButtonId[0]);
        final boolean isCorrect = getFlashcardEvaluation(splitButtonId[1]);

        evaluateFlashcard(buttonEvent, flashcardId, isCorrect);
    }

    /**
     * Gets flashcard evaluation from text
     *
     * @param flashcardEvaluation text containing flashcard evaluation
     *
     * @return true if flashcard was correctly answered, otherwise false
     */
    private boolean getFlashcardEvaluation(final String flashcardEvaluation) {
        return switch (flashcardEvaluation) {
            case DiscordConstants.CORRECT -> true;
            case DiscordConstants.INCORRECT -> false;
            default -> throw new IllegalStateException("Unexpected value: " + flashcardEvaluation);
        };
    }

    /**
     * Evaluates flashcard
     *
     * @param buttonEvent button interaction event
     * @param flashcardId flashcard ID to evaluate
     * @param isCorrect evaluation of the flashcard
     */
    private void evaluateFlashcard(final ButtonInteractionEvent buttonEvent,
                                   final long flashcardId,
                                   final boolean isCorrect) {
        try {
            leitnerSystemService.evaluateFlashcard(flashcardId, isCorrect);
        } catch (FlashcardNotFoundException | BoxNotFoundException e) {
            replyMessage(buttonEvent, e.getMessage());
        }
    }

}
