package com.example.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Question {
    private static int nextQuestionNumber = 1;

    protected final int questionNumber;
    private final String questionText;
    private final List<String> options;
    private final int answerIndex;

    public Question(String questionText, List<String> options, int answerIndex) {
        this(questionText, options, answerIndex, null);
    }

    @JsonCreator
    public Question(
        @JsonProperty("questionText") String questionText,
        @JsonProperty("options") List<String> options,
        @JsonProperty("answerIndex") int answerIndex,
        @JsonProperty("questionNumber") Integer questionNumber
    ) {
        
        this.questionNumber = resolveId(questionNumber);
        this.questionText = questionText;
        this.options = new ArrayList<>(options);
        this.answerIndex = answerIndex;
    }

    private static synchronized int resolveId(Integer requestedId) {
        if (requestedId == null) {
            return nextQuestionNumber++;
        }

        if (requestedId >= nextQuestionNumber) {
            nextQuestionNumber = requestedId + 1;
        }
        return requestedId;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getOptions() {
        return Collections.unmodifiableList(options);
    }

    public int getAnswerIndex() {
        return answerIndex;
    }
}
