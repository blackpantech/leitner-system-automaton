package com.blackpantech.leitner_system_automaton.infra.jpa.flashcards;

import com.blackpantech.leitner_system_automaton.infra.jpa.boxes.BoxEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "FLASHCARDS")
public class FlashcardEntity {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private long id;

    @Column(name = "QUESTION")
    private String question;

    @Column(name = "ANSWER")
    private String answer;

    @Column(name = "TAGS")
    private String[] tags;

    @ManyToOne
    @JoinColumn(name = "box_id")
    private BoxEntity currentBox;

    protected FlashcardEntity() {
        // default hibernate constructor
    }

    public FlashcardEntity(final String question, final String answer, final String[] tags, final BoxEntity currentBox) {
        this.question = question;
        this.answer = answer;
        this.tags = tags;
        this.currentBox = currentBox;
    }

    public BoxEntity getCurrentBox() {
        return currentBox;
    }

    public String[] getTags() {
        return tags;
    }

    public String getAnswer() {
        return answer;
    }

    public String getQuestion() {
        return question;
    }

    public long getId() {
        return id;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public void setCurrentBox(BoxEntity currentBox) {
        this.currentBox = currentBox;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FlashcardEntity flashcard = (FlashcardEntity) o;
        return id == flashcard.id &&
                Objects.equals(question, flashcard.question) &&
                Objects.equals(answer, flashcard.answer) &&
                Objects.deepEquals(tags, flashcard.tags) &&
                Objects.equals(currentBox, flashcard.currentBox);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, question, answer, Arrays.hashCode(tags), currentBox);
    }

}
