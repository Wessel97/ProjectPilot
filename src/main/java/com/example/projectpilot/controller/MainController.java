package com.example.projectpilot.controller;

import com.example.projectpilot.repository.TaskRepository;
import com.example.projectpilot.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {

    private UserRepository userRepository;
    private TaskRepository taskRepository;

    public MainController(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    public MainController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public MainController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public MainController () {
    }

    // Viser startsiden
    @GetMapping("/")
    public String showForside(Model model) {
        return "start";
    }

    // Viser alle Tasks
    @GetMapping ("/allTasks")
    public String showAllTasks(Model model) {
        model.addAttribute("tasks", taskRepository.getAllTasks());
        return "allTasks";
    }

    // Viser alle tasks for den enkelte users (MANGLER MODE)
    @GetMapping ("/userTasks")
    public String showUserTasks(Model model) {
        model.addAttribute("tasks", taskRepository.getAllTasksByUserID();
        return "userTasks";
    }
















}
