package com.example.ui;

import com.example.service.QuestionService;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Dimension;

public class AdminPanel extends JPanel {
    private final QuestionService questionService;

    private final JTabbedPane tabs;
    private final JTextField username = new JTextField();
    private final JTextField password = new JPasswordField();
    private final JButton loginButton = new JButton("Login");

    private final JTextField questionField = new JTextField();
    private final JTextField option1Field = new JTextField();
    private final JTextField option2Field = new JTextField();
    private final JTextField option3Field = new JTextField();
    private final JTextField option4Field = new JTextField();
    private final JComboBox<String> correctOptionBox = new JComboBox<>(new String[]{"Option 1", "Option 2", "Option 3", "Option 4"});

    public AdminPanel(QuestionService questionService, JTabbedPane tabs) {
        this.questionService = questionService;
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
            addQuestion();
        });
        
        revalidate();
        repaint();
        
    }

    private void addQuestion() {
        String adminUser = username.getText().trim();
        String adminPass = password.getText().trim();


        if (adminUser.equals("admin") && adminPass.equals("password")) {
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
