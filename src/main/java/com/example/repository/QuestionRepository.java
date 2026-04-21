package com.example.repository;

import com.example.model.Question;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class QuestionRepository {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final Path filePath;

    public QuestionRepository(String fullPath) {
        this.filePath = Paths.get(fullPath);
    }

    public List<Question> loadQuestions() {
        try {
            Path parent = filePath.getParent();
            if (parent != null && Files.notExists(parent)) {
                throw new RuntimeException("Questions folder not found: " + parent);
            } else if (Files.notExists(filePath)) {
                throw new RuntimeException("No question file detected at: " + filePath + ". Please create question.json.");
            } else if (Files.size(filePath) == 0) {
                return new ArrayList<>();
            }

            return OBJECT_MAPPER.readValue(filePath.toFile(), new TypeReference<List<Question>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Unable to read questions. Please check that the JSON format is valid.", e);
        }
    }

    public void addQuestion(Question question) {
        List<Question> questions = loadQuestions();
        questions.add(question);

        try {
            OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), questions);
        } catch (IOException e) {
            throw new RuntimeException("Unable to save question. Please check file permissions and path.", e);
        }
    }

    // public static void main(String[] args) {
        
    //     QuestionRepository repository = new QuestionRepository("C:\\Charan\\Virtusa\\TestCases\\Java\\quizapp\\src\\main\\resources\\questions.json");
    //     List<Question> questions = repository.loadQuestions();
    //     // System.out.println("Loaded " + questions.size() + " questions.");
    //     // for (Question q : questions) {
    //     //     System.out.println(q.getQuestionText());
    //     // }
    //     Question newQuestion = new Question("What is the capital of France?", List.of("London", "Berlin", "Paris", "Madrid"), 2,4);
    //     repository.addQuestion(newQuestion);
    //       System.out.println("Loaded " + questions.size() + " questions.");
    //     for (Question q : questions) {
    //         System.out.println(q.getQuestionText());
    //     }
    // }

}


