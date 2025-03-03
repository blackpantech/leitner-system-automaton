package com.blackpantech.leitner_system_automaton.infra.jpa.flashcards;

import com.blackpantech.leitner_system_automaton.infra.jpa.boxes.BoxEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "BOXES")
public class FlashcardEntity {

    @Id
    @GeneratedValue
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
}
