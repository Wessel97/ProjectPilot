package com.example.projectpilot.controller;

import com.example.projectpilot.model.Task;
import com.example.projectpilot.model.User;
import com.example.projectpilot.repository.TaskRepository;
import com.example.projectpilot.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class MainController {

    private UserRepository userRepository;
    private TaskRepository taskRepository;

    @Autowired
    public MainController(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }


    public MainController () {
    }

    @GetMapping("/")
    public String showStart(HttpSession session, Model model){
        //hvis username ikke er sat, så rediger til login
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
    public String showAddTask(Model model) {
        // Create a new Task object and add it to the model
        Task task = new Task();
        model.addAttribute("task", task);
        return "addTask";
    }

    // Poster ny task
    @PostMapping("/addTask")
    public String addTask(@RequestParam("task-title") String newTitle,
                          @RequestParam("task-description") String newDescription,
                          @RequestParam("task-note") String newNote,
                          @RequestParam("task-hours") int newHours,
                          @RequestParam("task-start_date") String newStartDateString, // Accept the date as a String
                          @RequestParam("task-end_date") String newEndDateString, // Accept the date as a String
                          @RequestParam("task-status") String newStatus,
                          @RequestParam("task-department") String newDepartment) {

        // Parse the start and end dates to java.sql.Date objects
        java.sql.Date newStartDate = java.sql.Date.valueOf(newStartDateString);
        java.sql.Date newEndDate = java.sql.Date.valueOf(newEndDateString);

        // Create a new Task
        Task newTask = new Task();
        newTask.setTitle(newTitle);
        newTask.setDescription(newDescription);
        newTask.setNote(newNote);
        newTask.setHours(newHours);
        newTask.setStart_Date(newStartDate);
        newTask.setEnd_Date(newEndDate);
        newTask.setStatus(newStatus);
        newTask.setDepartment(newDepartment);

        // Save the new task in the taskRepository
        taskRepository.addTask(newTask);

        // Redirect to the "allTasks" page
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
        if(!userRepository.verifyUser(email, password)){
            model.addAttribute("errorMessage", "Email or password invalid");
            return "login";
        }
        else
        {
            User user = userRepository.getUserByEmailAndPassword(email, password);
            session.setAttribute("user", user);
            return "redirect:/allTasks";
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
                               @RequestParam("password") String password,
                               Model model,
                               HttpSession session){
        //Check if user with mail already exists
        if(!userRepository.checkIfUserExists(email)){
            User user = new User(firstname, lastname, email, password);
            userRepository.addUser(user);
            session.setAttribute("user", user);
            return "redirect:/allTasks";
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
                                @RequestParam("task-start_date") @DateTimeFormat(pattern = "dd-MM-yyyy") Date updateStartDate,
                                @RequestParam("task-end_date") @DateTimeFormat(pattern = "dd-MM-yyyy") Date updateEndDate,
                                @RequestParam("task-status") String updateStatus,
                                @RequestParam("task-department") String updateDepartment) {
        //lav produkt ud fra parametre
        Task updateTask = new Task(updateTaskId, updateUserId, updateTitle, updateDescription, updateNote, updateHours, updatePayRate, updateStartDate, updateEndDate, updateStatus, updateDepartment);

        //kald opdater i repository
        taskRepository.updateTask(updateTask);

        //rediger til oversigtssiden
        return "redirect:/allTasks";
    }
    // Sletter en task
    @PostMapping("/deleteTask/{task_id}")
    public String deleteTask(@PathVariable("task_id") int taskId) {
        // Slet task med given taskId fra taskRepository
        taskRepository.deleteTaskByID(taskId);
        
        // Går tilbage til alle tasks
        return "redirect:/allTasks";
    }

    @GetMapping("/userTasks/{id}")
    public String showUserTasks(@PathVariable("id") int userId, HttpSession session) {
        List<Task> tasks = taskRepository.getAllTasksByUserID(userId);
        session.setAttribute("tasks", tasks);
        return "userTasks";
    }
}
