package com.example.service;

import com.example.model.Question;
import com.example.model.QuizResult;



import java.util.List;



public class QuizService {
    public QuizResult evaluate(List<Question> questions, List<Integer> selectedAnswers) {
        int total = questions.size();
        int correct = 0;

        for (int i = 0; i < total; i++) {
            Integer selected = selectedAnswers.get(i);
            if (selected != null && selected == questions.get(i).getAnswerIndex()) {
                correct++;
            }
        }

        int wrong = total - correct;
        double percentage = total == 0 ? 0 : (correct * 100.0) / total;
        String analysis = getPerformanceAnalysis(percentage);

        return new QuizResult(total, correct, wrong, percentage, analysis);
    }

    private String getPerformanceAnalysis(double percentage) {
        if (percentage >= 85) {
            return "Excellent performance";
        }
        if (percentage >= 65) {
            return "Good job. Keep practicing for higher scores";
        }
        if (percentage >= 40) {
            return "Average performance. Revise core concepts";
        }
        return "Needs improvement. Practice more fundamentals";
    }

  
}
