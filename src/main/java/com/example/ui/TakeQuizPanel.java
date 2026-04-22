package com.example.ui;

import com.example.model.Question;
import com.example.service.QuizService;
import com.example.service.UserService;
import  com.example.model.QuizResult;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JTextField;
import java.awt.Font;
import com.example.service.QuestionService;

public class TakeQuizPanel extends JPanel {
    private final QuizService quizService;
    private final QuestionService questionService;
    private final UserService userService;
    private final JTabbedPane tabs;

    private String name;
    private Long rollno;

    private final JLabel timerLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel questionCountLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel questionLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel nameLabel = new JLabel("Name: ", SwingConstants.CENTER);
    private final JLabel rollnoLabel = new JLabel("Roll No: ", SwingConstants.CENTER);

    private final JRadioButton option1 = new JRadioButton();
    private final JRadioButton option2 = new JRadioButton();
    private final JRadioButton option3 = new JRadioButton();
    private final JRadioButton option4 = new JRadioButton();


    private final ButtonGroup optionsGroup = new ButtonGroup();

    private final JButton startButton = new JButton("Start Quiz");
    private final JButton previousButton = new JButton("Previous");
    private final JButton nextButton = new JButton("Next");
    private final JButton submitButton = new JButton("Submit");

    private List<Question> questions  = new ArrayList<>();
    private List<Integer> selectedAnswers = new ArrayList<>();
    
    private final JTextField nameField = new JTextField();
    private final JTextField rollnoField = new JTextField();
    private int currentQuestionIndex = 0;

    
    private Timer timer;
    private int remainingSeconds = 1;

    public TakeQuizPanel(QuestionService questionService, QuizService quizService, UserService userService, JTabbedPane tabs) {
        this.questionService = questionService;
        this.quizService = quizService;
        this.userService = userService;
        this.tabs = tabs;

        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        initQuizUi();  
        
    }

    private void  initQuizUi(){
        JPanel userInfoPanel = new JPanel();
        nameField.setColumns(15);
        rollnoField.setColumns(10);
        userInfoPanel.add(nameLabel);
        userInfoPanel.add(nameField);
        userInfoPanel.add(rollnoLabel);
        userInfoPanel.add(rollnoField);

        timerLabel.setPreferredSize(new Dimension(100, 40));
        questionCountLabel.setPreferredSize(new Dimension(200, 40));
        questionLabel.setPreferredSize(new Dimension(800, 80));


        JPanel top = new JPanel(new BorderLayout());
        top.add(userInfoPanel, BorderLayout.CENTER);
        top.add(questionCountLabel, BorderLayout.WEST);
        top.add(timerLabel, BorderLayout.EAST);
        add(top,BorderLayout.NORTH);
        
        optionsGroup.add(option1);
        optionsGroup.add(option2);
        optionsGroup.add(option3);
        optionsGroup.add(option4);
    
        JPanel center = new JPanel();
        
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(questionLabel);
        center.add(Box.createVerticalStrut(10));
        center.add(option1);
        center.add(option2);
        center.add(option3);
        center.add(option4);
        add(center,BorderLayout.CENTER);
       
        JPanel bottom = new JPanel();
        bottom.add(startButton);
        bottom.add(previousButton);
        bottom.add(nextButton);
        bottom.add(submitButton);
        add(bottom, BorderLayout.SOUTH);
        
        previousButton.setEnabled(false);
        nextButton.setEnabled(false);
        submitButton.setEnabled(false);
        setOptionVisible(false);

        buttonEventListeners();

    }

    private void buttonEventListeners() {
       startButton.addActionListener(e -> {
            name = nameField.getText().trim();
            String rollnoText = rollnoField.getText().trim();
           
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your name.", "Input Required", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (rollnoText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your roll number.", "Input Required", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                rollno = Long.parseLong(rollnoText);
                 if (userService.checkIfUserPresent(rollno)){
                JOptionPane.showMessageDialog(this, "User already took quiz", "Duplicate User", JOptionPane.WARNING_MESSAGE);
                return;
            }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Roll number must be numeric.", "Invalid Roll Number", JOptionPane.WARNING_MESSAGE);
                return;
            }


            startQuiz();
        }
        );

        
        previousButton.addActionListener(e -> goToPreviousQuestion());
        nextButton.addActionListener(e -> goToNextQuestion());

        submitButton.addActionListener(e -> {
            if (currentQuestionIndex < questions.size() - 1){
                int confirm = JOptionPane.showConfirmDialog(this, "You have unanswered questions. Are you sure you want to submit?", "Confirm Submit", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.NO_OPTION) {
                    return;
                }
            }
            submitQuiz();
        });
       
    }

    private void startQuiz() {
        questions = questionService.getAllQuestions();
        List<Question> validQuestions = new ArrayList<>();
        for (Question question : questions) {
            if (question != null
                && question.getQuestionText() != null
                && !question.getQuestionText().isBlank()
                && question.getOptions() != null
                && question.getOptions().size() >= 4) {
                validQuestions.add(question);
            } 
        }
        questions = validQuestions;

        if (questions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No valid questions available. Add questions with 4 options from Admin tab first.", "No Questions", JOptionPane.WARNING_MESSAGE);
            return;
        }

        nameField.setVisible(false);
        rollnoField.setVisible(false);
        nameLabel.setText("");
        rollnoLabel.setText("");
        setOptionVisible(true);

        tabs.setEnabled(false);
        Collections.shuffle(questions);
        selectedAnswers = new ArrayList<>(Collections.nCopies(questions.size(), null));
        currentQuestionIndex = 0;

        remainingSeconds = questions.size() * 30;
        if (timer != null) {
            timer.stop();
        }

        timer = new Timer(1000, e -> {
            remainingSeconds--;
            updateTimer();
            if (remainingSeconds <= 0){
                timer.stop();
                JOptionPane.showMessageDialog(this, "Time is up. Submitting quiz.", "Timer", JOptionPane.INFORMATION_MESSAGE);
                submitQuiz();
            }
        });
        timer.start();

        startButton.setEnabled(false);
        previousButton.setEnabled(true);
        nextButton.setEnabled(true);
        submitButton.setEnabled(true);

        renderCurrentQuestion();
        updateTimer();
    }

    private void renderCurrentQuestion() {
        Question q = questions.get(currentQuestionIndex);
        List<String> options = q.getOptions();

        questionCountLabel.setText("Question" + (currentQuestionIndex + 1) + "/" + questions.size());
        questionLabel.setText( q.getQuestionText() );
        questionLabel.setFont(new Font("Arial", Font.BOLD, 18));


        option1.setText("1) " + options.get(0));
        option2.setText("2) " + options.get(1));
        option3.setText("3) " + options.get(2));
        option4.setText("4) " + options.get(3));

        optionsGroup.clearSelection();
        Integer selected = selectedAnswers.get(currentQuestionIndex);
        if (selected != null) {
            if (selected == 0) {
                option1.setSelected(true);
            } else if (selected == 1) {
                option2.setSelected(true);
            } else if (selected == 2) {
                option3.setSelected(true);
            } else if (selected == 3) {
                option4.setSelected(true);
            }
        }

    }

    private void goToPreviousQuestion(){
        saveCurrentSelection();
        if (currentQuestionIndex > 0){
            currentQuestionIndex--;
            renderCurrentQuestion();
        } else {
            JOptionPane.showMessageDialog(this, "You are on the first question.", "First Question", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void goToNextQuestion() {
        saveCurrentSelection();
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            renderCurrentQuestion();
        } else {
            JOptionPane.showMessageDialog(this, "You are on the last question. Click Submit to finish.", "Last Question", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void saveCurrentSelection() {
        Integer selected = null;
        if (option1.isSelected()) {
            selected = 0;
        } else if (option2.isSelected()) {
            selected = 1;
        } else if (option3.isSelected()) {
            selected = 2;
        } else if (option4.isSelected()) {
            selected = 3;
        }
        selectedAnswers.set(currentQuestionIndex, selected);
    }

    private void submitQuiz() {
        if (questions.isEmpty()) {
            return;
        }

        saveCurrentSelection();
        tabs.setEnabled(true);

        if (timer != null) {
            timer.stop();
        }

        QuizResult result = quizService.evaluate(questions, selectedAnswers);
        userService.addUser(name, rollno, selectedAnswers, questions);

        String summary = "Total Questions: " + result.getTotalQuestions()
            + "\nCorrect Answers: " + result.getCorrectAnswers()
            + "\nWrong Answers: " + result.getWrongAnswers()
            + "\nPercentage: " + String.format("%.2f", result.getPercentage()) + "%"
            + "\nPerformance: " + result.getAnalysis();

        JOptionPane.showMessageDialog(this, summary, "Quiz Result", JOptionPane.INFORMATION_MESSAGE);

        resetQuizState();
    }

    private void resetQuizState() {
        questions = new ArrayList<>();
        selectedAnswers = new ArrayList<>();
        currentQuestionIndex = 0;


        nameField.setText("Name: ");
        rollnoLabel.setText("Roll No: ");
        rollnoField.setText("");
        nameField.setText("");
        questionLabel.setText("");
        questionCountLabel.setText("");
        timerLabel.setText("");
        optionsGroup.clearSelection();
        option1.setText("");
        option2.setText("");
        option3.setText("");
        option4.setText("");

        startButton.setEnabled(true);
        previousButton.setEnabled(false);
        nextButton.setEnabled(false);
        submitButton.setEnabled(false);
        setOptionVisible(false);
    }

    private void setOptionVisible(boolean visible){
        option1.setVisible(visible);
        option2.setVisible(visible);
        option3.setVisible(visible);
        option4.setVisible(visible);
    }

    private void updateTimer() {
        int min = remainingSeconds / 60;
        int sec = remainingSeconds % 60;
        timerLabel.setText(String.format("Time Left: %02d:%02d", min, sec));
    }
}
