package com.example.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Users {
    private final String name;
    private final Long rollno;
    private final Map<Integer, Boolean> questions;
    private final int score;
    private final double percentage;

    @JsonCreator
    public Users(
        @JsonProperty("name") String name,
        @JsonProperty("rollno") Long rollno,
        @JsonProperty("questions") Map<Integer,Boolean> questionsAttempted,
        @JsonProperty("score") int score,
        @JsonProperty("percentage") double percentage
    ) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        } else if (rollno == null) {
            throw new IllegalArgumentException("Roll number cannot be null");
        } else if (questionsAttempted == null || questionsAttempted.isEmpty()) {
            throw new IllegalArgumentException("Questions cannot be null or empty");
        } else if (score < 0) {
            throw new IllegalArgumentException("Score cannot be negative");
        }

        this.name = name;
        this.rollno = rollno;
        this.questions = new HashMap<>(questionsAttempted);
        this.score = score;
        this.percentage = percentage;
    }

    public String getName() {
        return name;
    }

    public Long getRollno() {
        return rollno;
    }

    public Map<Integer, Boolean> getQuestions() {
        return Collections.unmodifiableMap(questions);
    }

    public int getScore() {
        return score;
    }

    public double getPercentage() {
        return percentage;
    }
   
}
