package com.example.projectpilot.controller;

// import com.example.projectpilot.model.Project; - IS NEVER USED
import com.example.projectpilot.model.Task;
import com.example.projectpilot.model.User;
import com.example.projectpilot.repository.ProjectRepository;
import com.example.projectpilot.repository.TaskRepository;
import com.example.projectpilot.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController
{

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    // Automatisk indsprøjtning af userRepository, taskRepository og projectRepository afhængighederne i klassen.
    @Autowired
    public UserController(UserRepository userRepository, TaskRepository taskRepository, ProjectRepository projectRepository)
    {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    // Viser startsiden.
    @GetMapping("/")
    public String showStart()
    {
        return "start";
    }

    // Viser loginsiden.
    @GetMapping("/login")
    public String showLogin()
    {
        return "login";
    }

    // Login af user.
    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        Model model,
                        HttpSession session)
    {
        if ( !userRepository.verifyUser(email, password) )
        {
            model.addAttribute("errorMessage", "Email or password invalid");
            return "/login";
        }
        else
        {
            User user = userRepository.getUserByEmailAndPassword(email, password);
            session.setAttribute("id", user.getId());
            if ( user.isAdmin() )
            {
                return "redirect:/adminStart";
            }
            else
            {
                return "redirect:/userStart";
            }
        }
    }

    // viser home siden.
    @GetMapping("/home")
    public String home(HttpSession session)
    {
        int userId = (int) session.getAttribute("id");
        User user = userRepository.getUserByID(userId);
        if ( user.isAdmin() )
        {
            return "redirect:/adminStart";
        }
        else
        {
            return "redirect:/userStart";
        }
    }

    // viser adminStart siden.
    @GetMapping("/adminStart")
    public String showAdminStart(HttpSession session, Model model)
    {
        if ( session.getAttribute("id") == null )
        {
            return "redirect:/";
        }

        model.addAttribute("project", projectRepository.getAllProjects());

        return "adminStart";
    }

    // Viser userStart siden.
    @GetMapping("/userStart")
    public String showUserStart(HttpSession session, Model model)
    {
        if ( session.getAttribute("id") == null )
        {
            return "redirect:/";
        }

        //Henter alle projekter.
        model.addAttribute("project", projectRepository.getAllProjects());

        return "userStart";
    }

    //Afslutter session, sender ikke data derfor ikke postmapping.
    @GetMapping("/logout")
    public String logout(HttpSession session)
    {
        session.invalidate();
        return "redirect:/";
    }

    // Viser register siden.
    @GetMapping("/register")
    public String showRegisterUser()
    {
        return "register";
        //Ingen HTTPSession her fordi alle skal kunne lave et login.
    }

    // Registrerer en ny user.
    @PostMapping("/register")
    public String registerUser(@RequestParam(value = "admin", defaultValue = "false") boolean admin,
                               @RequestParam("first_name") String firstname,
                               @RequestParam("last_name") String lastname,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password,
                               Model model,
                               HttpSession session)
    {
        if ( !userRepository.checkIfUserExists(email) )
        {
            User user = new User(admin, firstname, lastname, email, password);
            userRepository.addUser(user);
            session.setAttribute("id", user.getId());
            return "redirect:/adminStart";
        }
        else
        {
            model.addAttribute("errorMessage", "Email already in use");
            return "register";
        }
    }

    // Viser assignUser siden og sætter task_id i stien så vi åbner den specifikke task.
    @GetMapping("/assignUser/{id}")
    public String showAssignUser(@PathVariable("id") int task_Id, HttpSession session, Model model)
    {
        if ( session.getAttribute("id") == null )
        {
            return "redirect:/";
        }
        Task task = taskRepository.getTaskByTaskId(task_Id);
        model.addAttribute("task", task);
        model.addAttribute("users", userRepository.getAllUsers());

        return "assignUser";
    }

    // Assigner en task til en user.
    @PostMapping("/assignUser")
    public String assignUser(@RequestParam("task_id") int task_id, @RequestParam("userId") int userId, HttpSession session)
    {
        if ( session.getAttribute("id") == null )
        {
            return "redirect:/";
        }
        Task updateTask = taskRepository.getTaskByTaskId(task_id);
        taskRepository.assignTo(updateTask, userId);

        return "redirect:/allTasks";
    }

    // Viser alle users.
    @GetMapping("/allUsers")
    public String showAllUsers(HttpSession session, Model model)
    {
        if ( session.getAttribute("id") == null )
        {
            return "redirect:/";
        }
        model.addAttribute("user", userRepository.getAllUsers());
        return "allUsers";
    }

    // Sletter den valgte user.
    @PostMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") int userId, HttpSession session)
    {
        if ( session.getAttribute("id") == null )
        {
            return "redirect:/";
        }

        userRepository.deleteUserByID(userId);
        return "redirect:/allUsers";
    }

    // Viser updateUser siden.
    @GetMapping("/updateUser/{id}")
    public String showUpdateUser(@PathVariable("id") int id, HttpSession session, Model model)
    {
        if ( session.getAttribute("id") == null )
        {
            return "redirect:/";
        }
        User user = userRepository.getUserByID(id);
        model.addAttribute("user", user);

        return "updateUser";
    }

    //Opdaterer en user.
    @PostMapping("/updateUser")
    public String updateUser(@RequestParam("user-id") int id,
            /*Ved at sætte required = false bliver parameteren 'user-admin' valgfri,
            og controller-metoden vil ikke udløse en MissingServletRequestParameterException,
            hvis afkrydsningsfeltet ikke er markeret.*/
                            @RequestParam(value = "user-admin", required = false) boolean admin,
                            @RequestParam("user-firstname") String firstname,
                            @RequestParam("user-lastname") String lastname,
                            @RequestParam("user-email") String email,
                            @RequestParam("user-password") String password,
                            @RequestParam("user-confirm_password") String confirmPassword,
                           HttpSession session, Model model)
    {
        if ( session.getAttribute("id") == null )
        {
            return "redirect:/";
        }
        User user = userRepository.getUserByID(id);
        boolean passwordChanged = false;

        if (!password.isEmpty())
        {
            // Hvis nye passwords ikke matcher gives fejl og vi starter forfra på useren.
            if (!password.equals(confirmPassword)) {
                model.addAttribute("errorMessage", "Passwords do not match");
                model.addAttribute("user", user);
                return "updateUser";
            }
            user.setPassword(password);
            passwordChanged = true;
        }

        user.setAdmin(admin);
        user.setFirstName(firstname);
        user.setLastName(lastname);
        user.setEmail(email);

        userRepository.updateUser(user, passwordChanged);

        return "redirect:/allUsers";
    }


}
