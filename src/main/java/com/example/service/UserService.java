package com.example.service;

import com.example.model.Question;
import com.example.model.Users;
import com.example.repository.LoadFileRepository;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserService {

    private final LoadFileRepository<Users> loadFileRepository;
    private final QuizService quizService;
    

    public UserService(String filePath) {
        this.loadFileRepository = new LoadFileRepository<Users>(filePath, Users.class);
        this.quizService = new QuizService();
    }

    public boolean checkIfUserPresent(Long rollno){
        List<Users> users = loadFileRepository.loadAll();
        for (Users user : users){
            // System.out.println("Checking user with rollno: " + user.getRollno() + " against input rollno: " + rollno);
            if (user.getRollno().equals(rollno)){
                return true;
            }
        }
        return false;
    }

    public List<Users> getAllUsers(){
        return loadFileRepository.loadAll();
    }

    public void addUser(String name,Long rollno, List<Integer> selectedAnswers, List<Question> questions) {
    //    System.out.println("Adding user: " + name + ", Roll No: " + rollno);
       Map<Integer, Boolean> questionsAttempted = new HashMap<Integer,Boolean>();
       for (int i = 0; i < questions.size(); i++){
        Integer selected = selectedAnswers.get(i);
        boolean isCorrect = selected != null && selected == questions.get(i).getAnswerIndex();
        questionsAttempted.put(i + 1, isCorrect);
       }
       double percentage = quizService.evaluate(questions, selectedAnswers).getPercentage();
       int score = quizService.evaluate(questions, selectedAnswers).getCorrectAnswers();
    //    System.out.println("User " + name + " scored " + score + " with percentage: " + percentage);
       Users user = new Users(name, rollno, questionsAttempted, score, percentage);
       loadFileRepository.add(user);;
  
    }
    
}
