package com.example.ui;


import com.example.service.QuestionService;
import com.example.service.QuizService;
import com.example.service.UserService;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;


import java.awt.BorderLayout;

public class MainFrame extends JFrame {
    private final QuestionService questionService;
    private final UserService userService;

    // private final JTextField name = new JTextField();
    // private final JTextField rollno = new JTextField();

    public MainFrame() {
        this.questionService = new QuestionService("C:\\Charan\\Virtusa\\TestCases\\Java\\quizapp\\src\\main\\resources\\questions.json");
        this.userService = new UserService("C:\\Charan\\Virtusa\\TestCases\\Java\\quizapp\\src\\main\\resources\\users.json");

        setTitle("Welcome to Quiz App");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        QuizService quizService = new QuizService();
        JTabbedPane tabs = new JTabbedPane();

        // JPanel userInfoPanel = new JPanel();
        // name.setColumns(15);
        // rollno.setColumns(10);
        // userInfoPanel.add(new JLabel("Name:"));
        // userInfoPanel.add(name);
        // userInfoPanel.add(new JLabel("Roll No:"));
        // userInfoPanel.add(rollno);
        
        tabs.addTab("Take Quiz", new TakeQuizPanel(questionService,quizService, userService, tabs));
        tabs.addTab("Admin", new AdminPanel(questionService,userService, tabs));

        setLayout(new BorderLayout());
        // add(userInfoPanel, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }
}
