package com.blackpantech.leitner_system_automaton.infra.jpa.sessions;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "SESSIONS")
public class SessionEntity {

    @Id
    @Column(name = "ID")
    private int id;

    @Column(name = "SESSION_NUMBER")
    private int sessionNumber;

    protected SessionEntity() {
        // JPA no-args default constructor
    }

    public SessionEntity(final int sessionNumber) {
        this.sessionNumber = sessionNumber;
    }

    public int getSessionNumber() {
        return sessionNumber;
    }

    public void incrementSessionNumber() {
        sessionNumber++;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        SessionEntity session = (SessionEntity) o;
        return id == session.id && sessionNumber == session.sessionNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sessionNumber);
    }

}
