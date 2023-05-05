package com.example.projectpilot.controller;

import com.example.projectpilot.repository.TaskRepository;
import com.example.projectpilot.repository.UserRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class MainController {

    private UserRepository userRepository;
    private TaskRepository taskRepository;

    public MainController(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }


    @GetMapping("/")
    public String showUserList(Model model) {
        model.addAttribute("user", userRepository.getAllUsers());
        return "userlist";
    }








}
