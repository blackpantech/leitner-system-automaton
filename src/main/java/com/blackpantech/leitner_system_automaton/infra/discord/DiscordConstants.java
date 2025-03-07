package com.blackpantech.leitner_system_automaton.infra.discord;

import net.dv8tion.jda.api.entities.emoji.Emoji;

public class DiscordConstants {

    public final static String ID = "id";

    public final static String QUESTION = "question";

    public final static String ANSWER = "answer";

    public final static String TAGS = "tags";

    public final static String TAG = "tag";

    public final static String SESSION = "session";

    public final static String CORRECT = "correct";

    public final static Emoji POSITIVE_CHECKMARK = Emoji.fromUnicode("U+2705");

    public final static String INCORRECT = "incorrect";

    public final static Emoji NEGATIVE_CHECKMARK = Emoji.fromUnicode("U+274E");

    public final static String CREATE_FLASHCARD_EVENT = "createflashcard";

    public final static String EDIT_FLASHCARD_EVENT = "editflashcard";

    public final static String GET_FLASHCARD_EVENT = "getflashcard";

    public final static String DELETE_FLASHCARD_EVENT = "deleteflashcard";

    public final static String GET_ALL_FLASHCARDS_EVENT = "getallflashcards";

    public final static String GET_ALL_FLASHCARDS_WITH_TAG_EVENT = "getallflashcardswithtag";

    public final static String GET_SESSION_QUESTIONNAIRE_EVENT = "getsessionquestionnaire";

    public final static String GET_SESSION_QUESTIONNAIRE_WITH_TAG_EVENT = "getsessionquestionnairewithtag";

}
