package com.example.projectpilot.controller;

import com.example.projectpilot.model.Task;
import com.example.projectpilot.model.User;
import com.example.projectpilot.repository.TaskRepository;
import com.example.projectpilot.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.sql.Date;

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
    public String showStart(Model model) {
        model.addAttribute("task", taskRepository.getAllTasks());
        return "allTasks";
    }

    // Pga. testing purposes så er startsiden nu en side der viser add tasks
    @GetMapping("addTask")
    public String showAddTask() {
        return "addTask";
    }

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
        return "addTask";
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

    //Viser login-siden
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    // Skal logge useren ind (Mangler kode)
    @PostMapping("/login")
    public String login() {
        return "userTasks";
    }


    // Viser alle Tasks
    @GetMapping ("/allTasks")
    public String showAllTasks(Model model) {
        model.addAttribute("tasks", taskRepository.getAllTasks());
        return "allTasks";
    }

    // Viser alle tasks for den enkelte user der er logget ind (MANGLER MODE)
    @GetMapping ("/userTasks")
    public String showUserTasks(int id, Model model) {
        model.addAttribute("tasks", taskRepository.getAllTasksByUserID(id));
        return "userTasks";
    }


    @GetMapping("/updateTask/{id}")
    public String showUpdate(@PathVariable("id") int updateId, Model model) {
        Task updateTask = taskRepository.getTaskByTaskId(updateId);

        model.addAttribute("product", updateTask);

        return "updateTask";
    }

    @PostMapping("/updateTask")
    public String updateTask(@RequestParam("task-title") String updateTitle,
                             @RequestParam("task-description") String updateDescription,
                             @RequestParam("task-note") String updateNote,
                             @RequestParam("task-hour") int updateHour,
                             @RequestParam("task-flag") boolean updateFlag,
                             @RequestParam("task-startDate") String updateStartDate,
                             @RequestParam("task-endDate") String updateEndDate,
                             @RequestParam("task-status") String updateStatus,
                             @RequestParam("task-department") String updateDepartment) {
        Task updateTask = new Task(updateTitle, updateDescription, updateNote, updateHour, updateFlag, updateStartDate, updateEndDate, updateStatus, updateDepartment);

        taskRepository.updateTask(updateTask);

        return "redirect:/allTasks";
    }



















}
