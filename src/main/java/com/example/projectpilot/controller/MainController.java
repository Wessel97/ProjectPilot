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
import java.time.LocalDate;
import java.util.List;

@Controller
public class MainController
{

    private UserRepository userRepository;
    private TaskRepository taskRepository;

    @Autowired
    public MainController(UserRepository userRepository, TaskRepository taskRepository)
    {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }


    public MainController()
    {
    }

    @GetMapping("/")
    public String showStart()
    {
        return "start";
    }

    // Viser alle tasks
    @GetMapping("/allTasks")
    public String showAllTasks(HttpSession session, Model model)
    {
        if ( session.getAttribute("user") == null )
        {
            return "redirect:/";
        }
        model.addAttribute("task", taskRepository.getAllTasks());
        return "allTasks";
    }

    @GetMapping("/allUsers")
    public String showAllUsers(HttpSession session, Model model)
    {
        if ( session.getAttribute("user") == null )
        {
            return "redirect:/";
        }
        model.addAttribute("user", userRepository.getAllUsers());
        return "allUsers";
    }

    // Viser add tasks siden
    @GetMapping("/addTask")
    public String showAddTask(HttpSession session, Model model)
    {
        if ( session.getAttribute("user") == null )
        {
            return "redirect:/";
        }
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
                          @RequestParam("task-start_date") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate newStartDate,
                          @RequestParam("task-end_date") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate newEndDate,
                          @RequestParam("task-status") String newStatus,
                          @RequestParam("task-department") String newDepartment,
                          HttpSession session)
    {
        if ( session.getAttribute("user") == null )
        {
            return "redirect:/";
        }

        // Convert LocalDate to java.sql.Date
        Date sqlStartDate = Date.valueOf(newStartDate);
        Date sqlEndDate = Date.valueOf(newEndDate);

        // Laver en ny Task
        Task newTask = new Task();
        newTask.setTitle(newTitle);
        newTask.setDescription(newDescription);
        newTask.setNote(newNote);
        newTask.setHours(newHours);
        newTask.setStart_Date(sqlStartDate);
        newTask.setEnd_Date(sqlEndDate);
        newTask.setStatus(newStatus);
        newTask.setDepartment(newDepartment);

        // Gemmer i taskRepository
        taskRepository.addTask(newTask);

        // Går tilbage til alle tasks
        return "redirect:/allTasks";
    }


    // Viser "opret bruger" siden
    @GetMapping("/addUser")
    public String showAddUser(HttpSession session, Model model)
    {
        if ( session.getAttribute("user") == null )
        {
            return "redirect:/";
        }

        return "addUser";
    }

    // Opretter den nye user til user repository (med HTML form)
    @PostMapping("/addUser")
    public String addUser(@RequestParam("user-firstname") String newFirstName,
                          @RequestParam("user-lastname") String newLastName,
                          @RequestParam("user-email") String newEmail,
                          @RequestParam("user-password") String newPassword,
                          HttpSession session)
    {
        if ( session.getAttribute("user") == null )
        {
            return "redirect:/";
        }

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


    @GetMapping("/login")
    public String showLogin()
    {
        return "login";
        //Ingen HttpSession her da man her logger ind.
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        Model model,
                        HttpSession session)
    {
        //Check if user with mail already exists
        if ( !userRepository.verifyUser(email, password) )
        {
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
    public String logout(HttpSession session)
    {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/register")
    public String registerUser()
    {
        return "register";
        //Ingen HTTPSession her fordi alle skal kunne lave et login.
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam("first_name") String firstname,
                               @RequestParam("last_name") String lastname,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password,
                               Model model,
                               HttpSession session)
    {
        //Check if user with mail already exists
        if ( !userRepository.checkIfUserExists(email) )
        {
            User user = new User(firstname, lastname, email, password);
            userRepository.addUser(user);
            session.setAttribute("user", user);
            return "redirect:/allTasks";
        }
        else
        {
            model.addAttribute("errorMessage", "Email already in use");
            return "register";
        }
    }

    // Viser update task siden
    @GetMapping("/updateTask/{task_id}")
    public String showUpdateTask(@PathVariable("task_id") int updateId, HttpSession session, Model model)
    {
        if ( session.getAttribute("user") == null )
        {
            return "redirect:/";
        }

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
                             @RequestParam("task-start_date") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate updateStartDate,
                             @RequestParam("task-end_date") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate updateEndDate,
                             @RequestParam("task-status") String updateStatus,
                             @RequestParam("task-department") String updateDepartment,
                             HttpSession session)
    {
        if ( session.getAttribute("user") == null )
        {
            return "redirect:/";
        }

        // Convert LocalDate to java.sql.Date
        Date sqlStartDate = Date.valueOf(updateStartDate);
        Date sqlEndDate = Date.valueOf(updateEndDate);


        //lav produkt ud fra parametre
        Task updateTask = new Task(updateTaskId, updateUserId, updateTitle, updateDescription, updateNote, updateHours, updatePayRate, sqlStartDate, sqlEndDate, updateStatus, updateDepartment);

        //kald opdater i repository
        taskRepository.updateTask(updateTask);

        //rediger til oversigtssiden
        return "redirect:/allTasks";
    }

    // Sletter en task
    @PostMapping("/deleteTask/{task_id}")
    public String deleteTask(@PathVariable("task_id") int taskId, HttpSession session)
    {
        if ( session.getAttribute("user") == null )
        {
            return "redirect:/";
        }

        // Slet task med given taskId fra taskRepository
        taskRepository.deleteTaskByID(taskId);

        // Går tilbage til alle tasks
        return "redirect:/allTasks";
    }

    @GetMapping("/userTasks/")
    public String showUserTasks(HttpSession session,Model model)
    {
        if ( session.getAttribute("user") == null )
        {
            return "redirect:/";
        }

        User user = (User) session.getAttribute("user");
        List<Task> taskList = taskRepository.getAllTasksByUserID(user.getID());
        // Den her gør ikke noget, så den skal nok bare slettes tilsidsts
        //session.setAttribute("taskList", taskList);
        model.addAttribute("task", taskList);

        return "userTasks";
    }



}
