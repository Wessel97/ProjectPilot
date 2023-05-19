package com.example.projectpilot.controller;

import com.example.projectpilot.model.Department;
import com.example.projectpilot.model.Project;
import com.example.projectpilot.model.Task;
import com.example.projectpilot.model.User;
import com.example.projectpilot.repository.DepartmentRepository;
import com.example.projectpilot.repository.ProjectRepository;
import com.example.projectpilot.repository.TaskRepository;
import com.example.projectpilot.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.server.PathParam;
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
    private DepartmentRepository departmentRepository;
    private ProjectRepository projectRepository;

    @Autowired
    public MainController(UserRepository userRepository, TaskRepository taskRepository,
                          DepartmentRepository departmentRepository, ProjectRepository projectRepository)
    {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.departmentRepository = departmentRepository;
        this.projectRepository = projectRepository;
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

    @GetMapping("/admin")
    public String admin(HttpSession session)
    {
        User user = (User) session.getAttribute("user");
        if ( user == null || user.getId() != 1 )
        {
            return "redirect:/";
        }

        return "admin";
    }

    @GetMapping("/allDepartments")
    public String allDepartments(HttpSession session, Model model)
    {
        if(session.getAttribute("user") == null )
        {
            return "redirect:/";
        }
        model.addAttribute("department", departmentRepository.getAllDepartments());

        return "allDepartments";
    }

    @GetMapping("/addDepartment")
    public String addDepartment(HttpSession session, Model model)
    {
        if ( session.getAttribute("user") == null )
        {
            return "redirect:/";
        }
        // Create a new Task object and add it to the model

        return "addDepartment";
    }

    @GetMapping("/showDepartment/{id}")
    public String showDepartment(@PathVariable("id") int id, HttpSession session, Model model)
    {
        if ( session.getAttribute("user") == null)
        {
            return "redirect:/";
        }

        List<Task> taskList = taskRepository.getAllTasksByDepartmentID(id);
        model.addAttribute("task", taskList);

        return "showDepartment";
    }

    //
    //

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
    @GetMapping("/updateTask/{id}")
    public String showUpdateTask(@PathVariable("id") int updateId, HttpSession session, Model model)
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

    @GetMapping("/assignUser/{id}")
    public String showAssignUser(@PathVariable("id") int task_Id, HttpSession session, Model model)
    {
        if ( session.getAttribute("user") == null )
        {
            return "redirect:/";
        }
        Task task = taskRepository.getTaskByTaskId(task_Id);

        // Set the task object as a model attribute
        model.addAttribute("task", task);
        model.addAttribute("users", userRepository.getAllUsers());
        return "assignUser";
    }

    @PostMapping("/assignUser")
    // Fejlen er umiddelbart at userId kun får 1, selvom kan man se på htmlen at den henter forskellige userid'er
    // muligvis er det RequestParam??
    public String assignUser(@RequestParam("task_id") int task_id, @RequestParam("userId") int userId, HttpSession session)
    {
        if ( session.getAttribute("user") == null )
        {
            return "redirect:/";
        }
        Task updateTask = taskRepository.getTaskByTaskId(task_id);

        taskRepository.assignTo(updateTask, userId);

        return "redirect:/allTasks";
    }


    // Sletter en task
    @PostMapping("/deleteTask/{id}")
    public String deleteTask(@PathVariable("id") int taskId, HttpSession session)
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

    @GetMapping("/userTasks")
    public String showUserTasks(HttpSession session, Model model)
    {
        if ( session.getAttribute("user") == null )
        {
            return "redirect:/";
        }

        User user = (User) session.getAttribute("user");
        List<Task> taskList = taskRepository.getAllTasksByUserID(user.getId());
        // Den her gør ikke noget, så den skal nok bare slettes tilsidsts
        //session.setAttribute("taskList", taskList);
        model.addAttribute("task", taskList);

        return "userTasks";
    }


    @GetMapping("/allProjects")
    public String showAllProjects(HttpSession session, Model model)
    {
        if ( session.getAttribute("user") == null )
        {
            return "redirect:/";
        }
        model.addAttribute("project", projectRepository.getAllProjects());
        return "allProjects";
    }

    @GetMapping("/showProject/{id}")
    public String showProject(@PathVariable("id") int projectId, HttpSession session, Model model)
    {
        if ( session.getAttribute("user") == null )
        {
            return "redirect:/";
        }
        Project project = projectRepository.getProjectByID(projectId);
        model.addAttribute("project", project);
        return "showProject";
    }

    @GetMapping("/addProject")
    public String showAddProject(HttpSession session, Model model)
    {
        if ( session.getAttribute("user") == null )
        {
            return "redirect:/";
        }
        model.addAttribute("project", new Project());
        return "addProject";
    }

    @PostMapping("/addProject")
    public String addProject(@RequestParam("projectName") String projectName, HttpSession session)
    {
        if ( session.getAttribute("user") == null )
        {
            return "redirect:/";
        }
        Project newProject = new Project();
        newProject.setProjectName(projectName);
        projectRepository.addProject(newProject);
        return "redirect:/allProjects";
    }

    @GetMapping("/updateProject/{id}")
    public String showUpdateProject(@PathVariable("id") int projectId, HttpSession session, Model model)
    {
        if ( session.getAttribute("user") == null )
        {
            return "redirect:/";
        }
        Project project = projectRepository.getProjectByID(projectId);
        model.addAttribute("project", project);
        return "updateProject";
    }

    @PostMapping("/updateProject")
    public String updateProject(@RequestParam("projectID") int projectId, @RequestParam("projectName") String projectName, HttpSession session)
    {
        if ( session.getAttribute("user") == null )
        {
            return "redirect:/";
        }
        Project updateProject = new Project(projectId, projectName);
        projectRepository.updateProject(updateProject);
        return "redirect:/allProjects";
    }


}
