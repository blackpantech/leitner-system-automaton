package com.blackpantech.leitner_system_automaton.infra.jpa.boxes;

import com.blackpantech.leitner_system_automaton.infra.jpa.flashcards.FlashcardEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "BOXES")
public class BoxEntity {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "FREQUENCY")
    private int frequency;

    @OneToMany(mappedBy = "currentBox")
    private final Set<FlashcardEntity> flashcards = new HashSet<>();

    protected BoxEntity() {

    }

    public BoxEntity(final int frequency) {
        this.frequency = frequency;
    }

    public Set<FlashcardEntity> getFlashcards() {
        return flashcards;
    }

    public int getFrequency() {
        return frequency;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BoxEntity boxEntity = (BoxEntity) o;
        return id == boxEntity.id &&
                frequency == boxEntity.frequency &&
                Objects.equals(flashcards, boxEntity.flashcards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, frequency, flashcards);
    }

}
