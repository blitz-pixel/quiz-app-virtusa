package com.example.model;

public class QuizResult {
    private final int totalQuestions;
        private final int correctAnswers;
        private final int wrongAnswers;
        private final double percentage;
        private final String analysis;

        public QuizResult(int totalQuestions, int correctAnswers, int wrongAnswers, double percentage, String analysis) {
            this.totalQuestions = totalQuestions;
            this.correctAnswers = correctAnswers;
            this.wrongAnswers = wrongAnswers;
            this.percentage = percentage;
            this.analysis = analysis;
        }

        public int getTotalQuestions() {
            return totalQuestions;
        }

        public int getCorrectAnswers() {
            return correctAnswers;
        }

        public int getWrongAnswers() {
            return wrongAnswers;
        }

        public double getPercentage() {
            return percentage;
        }

        public String getAnalysis() {
            return analysis;
        }
}
