package com.example.service;

import com.example.model.Question;
import com.example.repository.LoadFileRepository;

import java.util.Arrays;
import java.util.List;

public class QuestionService{
    // private final QuestionRepository questionRepository;
    private final LoadFileRepository<Question> loadFileRepository;
    


    public QuestionService(String filePath) {
        this.loadFileRepository = new LoadFileRepository<Question>(filePath, Question.class);
    }

    public List<Question> getAllQuestions() {
        return loadFileRepository.loadAll();
    }

    public void addQuestion(String questionText, String option1, String option2, String option3, String option4, int answerIndex) {
        List<String> options = Arrays.asList(option1, option2, option3, option4);
        if (questionText == null || questionText.isBlank()) {
            throw new IllegalArgumentException("Question cannot be null or empty");
        } else if (options == null || options.size() > 4) {
            throw new IllegalArgumentException("Options must be provided and cannot exceed 4");
        } else if (answerIndex < 0 || answerIndex > 3) {
            throw new IllegalArgumentException("Answer index must be between 0 and 3");
        }
        Question question = new Question(questionText, options, answerIndex);
        loadFileRepository.add(question);
    }
}