package com.example.ui;

import com.example.model.Question;
import com.example.model.Users;
import com.example.service.QuestionService;
import com.example.service.UserService;
import com.sun.tools.javac.Main;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class AdminPanel extends JPanel {
    private final QuestionService questionService;
    private final UserService userService;

    private String usernameMatch;
    private String passwordMatch;

    private final JTabbedPane tabs;
    private final JTextField username = new JTextField();
    private final JTextField password = new JPasswordField();
    private final JButton loginButton = new JButton("Login");
    private final JButton addQuestionButton = new JButton("Add Question");
    private final JButton viewAllStudentsButton = new JButton("View All Students");
    private final JButton viewAllQuestionsButton = new JButton("View All Questions");

    private final JTextField questionField = new JTextField();
    private final JTextField option1Field = new JTextField();
    private final JTextField option2Field = new JTextField();
    private final JTextField option3Field = new JTextField();
    private final JTextField option4Field = new JTextField();
    private final JComboBox<String> correctOptionBox = new JComboBox<>(new String[]{"Option 1", "Option 2", "Option 3", "Option 4"});

    public AdminPanel(QuestionService questionService,UserService userService, JTabbedPane tabs) {
        this.questionService = questionService;
        this.userService = userService;
        this.tabs = tabs;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        initLoginUi(); 
    }

    private void initLoginUi() {
        removeAll();
        
        JPanel adminUsernamePanel = field("Admin Username", username);
        add(adminUsernamePanel);
        JPanel adminPasswordPanel = field("Admin Password", password);
        add(adminPasswordPanel);

        add(loginButton, BorderLayout.CENTER);
        loginButton.addActionListener(e -> {
            initAdminUi();
        });
        
        revalidate();
        repaint();
        
    }

    private void initAdminUi() {
        removeAll();

        add(addQuestionButton, BorderLayout.CENTER);
        add(viewAllStudentsButton, BorderLayout.CENTER);
        add(viewAllQuestionsButton, BorderLayout.CENTER);

        addQuestionButton.addActionListener(e -> {
            addQuestion();
        });

        viewAllStudentsButton.addActionListener(e -> {
            viewAllStudents();
        });

        viewAllQuestionsButton.addActionListener(e -> {
            ShowQuestions();
        });

        revalidate();
        repaint();
    }

    private void viewAllStudents(){
        removeAll();
        List<Users> users = userService.getAllUsers();
        int questionSize = questionService.getAllQuestions().size();

        List<String> columnNames = new ArrayList<>(List.of("Name", "Roll No"));
        for (int i = 1; i <= questionSize; i++) {
            columnNames.add("Q" + i);
        }
        columnNames.addAll(List.of("Score", "Percentage"));
        String[] columns = columnNames.toArray(new String[0]);
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

        for (Users user : users){
            List<Object> rowData = new ArrayList<>();
            rowData.add(user.getName());
            rowData.add(user.getRollno());
            Map<Integer, Boolean> questionsAttempted = user.getQuestions();
        
            for (int i = 1; i <= questionSize; i++) {
                Boolean isCorrect = questionsAttempted.get(i);
                Boolean result = isCorrect == true;
                rowData.add(result);
            }
            rowData.add(user.getScore());
            rowData.add(String.format("%.2f%%", user.getPercentage()));
            tableModel.addRow(rowData.toArray());
        }

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

         
         JButton backButton = new JButton("Back");
         add(backButton, BorderLayout.SOUTH);
         backButton.addActionListener(e -> {
            initLoginUi();
         });
        revalidate();
        repaint();

    }

    private void ShowQuestions() {
        removeAll();
        List<Question> questions = questionService.getAllQuestions();

        String[] columns = {"S.no", "Question", "Option 1", "Option 2", "Option 3", "Option 4", "Correct Option"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

        for (Question question : questions){
            List<Object> rowData = new ArrayList<>();
            rowData.add(question.getQuestionNumber());
            rowData.add(question.getQuestionText());
            rowData.addAll(question.getOptions());
            rowData.add((question.getAnswerIndex()) + 1);
            tableModel.addRow(rowData.toArray());
        }

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

         
         JButton backButton = new JButton("Back");
         add(backButton, BorderLayout.SOUTH);
         backButton.addActionListener(e -> {
            initLoginUi();
         });
        revalidate();
        repaint();

    }

    private void addQuestion() {
        String adminUsername = username.getText().trim();
        String adminPassword = password.getText().trim();

        Properties prop = new Properties();
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                JOptionPane.showMessageDialog(this, "Some error happened", "Error", JOptionPane.ERROR_MESSAGE);
                // System.out.println("Sorry, unable to find config.properties");
                return;
            }
            prop.load(input);
            usernameMatch = prop.getProperty("username");
            passwordMatch = prop.getProperty("password");
        } catch (IOException ex) {
            // System.out.println("Error reading config file: " + ex.getMessage());
            return;
        }


        if (adminUsername.equals(usernameMatch) && adminPassword.equals(passwordMatch)) {
            username.setText("");
            password.setText("");

            tabs.setEnabled(false);
            removeAll();
            revalidate();
            repaint();

            add(field("Question", questionField));
            add(field("Option 1", option1Field));
            add(field("Option 2", option2Field));
            add(field("Option 3", option3Field));
            add(field("Option 4", option4Field));

          
            JPanel correctPanel = new JPanel();
            correctPanel.setLayout(new BoxLayout(correctPanel, BoxLayout.X_AXIS));
            JLabel correctLabel = new JLabel("Correct Option: ");
            correctLabel.setPreferredSize(new Dimension(150, 30));
            correctPanel.add(correctLabel);
            correctPanel.add(correctOptionBox);
            correctPanel.setMaximumSize(new Dimension(700, 40));
            add(correctPanel);

            JButton saveButton = new JButton("Save Question");
            JButton cancelButton = new JButton("Cancel");

            saveButton.addActionListener(e -> saveQuestion());
            cancelButton.addActionListener(e -> {
                tabs.setEnabled(true);
                clearFields();
                initLoginUi();
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);
            add(buttonPanel);

        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
    private JPanel field(String label, JTextField textField) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        JLabel jLabel = new JLabel(label + ": ");;
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel.setPreferredSize(new Dimension(150, 30));
        textField.setMaximumSize(new Dimension(500, 30));
        panel.add(jLabel);
        panel.add(textField);
        panel.setMaximumSize(new Dimension(700, 40));
        return panel;
    }

    private void saveQuestion() {
        String questionText = questionField.getText().trim();
        String option1 = option1Field.getText().trim();
        String option2 = option2Field.getText().trim();
        String option3 = option3Field.getText().trim();
        String option4 = option4Field.getText().trim();

        if (questionText.isBlank() || option1.isBlank() || option2.isBlank() || option3.isBlank() || option4.isBlank()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int correctIndex = correctOptionBox.getSelectedIndex();
        questionService.addQuestion(questionText, option1, option2, option3, option4, correctIndex);

        clearFields();
        JOptionPane.showMessageDialog(this, "Question saved successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        
        tabs.setEnabled(true);
        initLoginUi();
    }

  

    private void clearFields() {
        questionField.setText("");
        option1Field.setText("");
        option2Field.setText("");
        option3Field.setText("");
        option4Field.setText("");
        correctOptionBox.setSelectedIndex(0);
    }
}
