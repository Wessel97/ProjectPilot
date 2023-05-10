package com.example.projectpilot.controller;

import com.example.projectpilot.model.Task;
import com.example.projectpilot.model.User;
import com.example.projectpilot.repository.TaskRepository;
import com.example.projectpilot.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.SQLException;

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

    @Autowired
    public MainController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public MainController () {
    }
    
    @GetMapping("/")
    public String showStart(HttpSession session, Model model){
        //hvis username ikke er sat, så rediriger til login
        if (session.getAttribute("email") == null){
            return "redirect:/login";
        }
        return "start";
    }

    // Viser alle tasks
    @GetMapping("/allTasks")
    public String showAllTasks(Model model) {
        model.addAttribute("task", taskRepository.getAllTasks());
        return "allTasks";
    }

    // Viser add tasks siden
    @GetMapping("/addTask")
    public String showAddTask() {
        return "addTask";
    }

    // Poster ny task
    @PostMapping("/addTask")
    public String addTask(@RequestParam("task-title") String newTitle,
                          @RequestParam("task-description") String newDescription,
                          @RequestParam("task-note") String newNote,
                          @RequestParam("task-hours") int newHours,
                          @RequestParam("task-start_date") String newStartDate,
                          @RequestParam("task-end_date") String newEndDate,
                          @RequestParam("task-status") String newStatus,
                          @RequestParam("task-department") String newDepartment) {
        // Laver en ny Task
        Task newTask = new Task();
        newTask.setTitle(newTitle);
        newTask.setDescription(newDescription);
        newTask.setNote(newNote);
        newTask.setHours(newHours);
        newTask.setStart_Date(newStartDate);
        newTask.setEnd_Date(newEndDate);
        newTask.setStatus(newStatus);
        newTask.setDepartment(newDepartment);

        // Gemmer i taskRepository
        taskRepository.addTask(newTask);

        // Går tilbage til alle tasks
        return "redirect:/allTasks";
    }




    // Viser "opret bruger" siden
    @GetMapping("/addUser")
    public String showAddUser(Model model) {
        return "addUser";
    }

    // Opretter den nye user til user repository (med HTML form)
    @PostMapping("/addUser")
    public String addUser(@RequestParam("user-firstname") String newFirstName,
                          @RequestParam("user-lastname") String newLastName,
                          @RequestParam("user-email") String newEmail,
                          @RequestParam("user-password") String newPassword) {
        //lave en ny User
        User newUser = new User();
        newUser.setFirstName(newFirstName);
        newUser.setLastName(newLastName);
        newUser.setEmail(newEmail);
        newUser.setPassword(newPassword);

        //Gem ny User
        userRepository.addUser(newUser);

        //Tilbage til start så man kan logge ind
        return "start";
    }


    @GetMapping ("/login")
    public String showLogin(){
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        Model model,
                        HttpSession session){
        //Check if user with mail already exists
        if(!userRepository.verifyUser(password, email)){
            model.addAttribute("errorMessage", "Email or password invalid");
            return "login";
        }
        else
        {
            User user = userRepository.getUserByEmailAndPassword(email, password);
            session.setAttribute("user", user);
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/register")
    public String registerUser(){
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam("first_name") String firstname,
                               @RequestParam("last_name") String lastname,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password, Model model){
        //Check if user with mail already exists
        if(!userRepository.checkIfUserExists(email)){
            User user = new User(firstname, lastname, email, password);
            userRepository.addUser(user);
            return "redirect:/";
        }else {
            model.addAttribute("errorMessage", "Email already in use");
            return "register";
        }
    }

    // Viser update task siden
    @GetMapping("/updateTask/{task_id}")
    public String showUpdateTask(@PathVariable("task_id") int updateId, Model model) {
        //find produkt med id=updateId i databasen
        Task updateTask = taskRepository.getTaskByTaskId(updateId);

        //tilføj produkt til viewmodel, så det kan bruges i Thymeleaf
        model.addAttribute("task", updateTask);

        //fortæl Spring hvilken HTML-side, der skal vises
        return "updateTask";
    }

    // Poster update til eksisterende task (UDEN FLAG)
    @PostMapping("/updateTask")
    public String updateTask(@RequestParam("task-task_id") int updateTaskId,
                                @RequestParam("task-user_id") int updateUserId,
                                @RequestParam("task-title") String updateTitle,
                                @RequestParam("task-description") String updateDescription,
                                @RequestParam("task-note") String updateNote,
                                @RequestParam("task-hours") int updateHours,
                                @RequestParam("task-pay_rate") int updatePayRate,
                                @RequestParam("task-start_date") String updateStartDate,
                                @RequestParam("task-end_date") String updateEndDate,
                                @RequestParam("task-status") String updateStatus,
                                @RequestParam("task-department") String updateDepartment) {
        //lav produkt ud fra parametre
        Task updateTask = new Task(updateTaskId, updateUserId, updateTitle, updateDescription, updateNote, updateHours, updatePayRate, updateStartDate, updateEndDate, updateStatus, updateDepartment);

        //kald opdater i repository
        taskRepository.updateTask(updateTask);

        //rediriger til oversigtssiden
        return "redirect:/allTasks";
    }


}
