package com.example.projectpilot.controller;

import com.example.projectpilot.model.Project;
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

    Project newProject = new Project();



    @Autowired
    public UserController(UserRepository userRepository, TaskRepository taskRepository, ProjectRepository projectRepository)
    {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    @GetMapping("/")
    public String showStart()
    {
        return "start";
    }

    // This method is used to show the login page.
    @GetMapping("/login")
    public String showLogin()
    {
        return "login";
        //Ingen HttpSession her da man her logger ind.
    }

    // This method is used to verify the user.
    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        Model model,
                        HttpSession session)
    {
        //Check if user with mail already exists
        if ( !userRepository.verifyUser(email, password))
        {
            model.addAttribute("errorMessage", "Email or password invalid");
            return "/login";
        }
        else
        {
            User user = userRepository.getUserByEmailAndPassword(email, password);
            session.setAttribute("user", user);
            if (user.isAdmin())
            {
                return "redirect:/adminStart";
            }
            else
            {
                return "redirect:/userStart";
            }
        }
    }

    @GetMapping("/adminStart")
    public String showAdminStart(HttpSession session, Model model)
    {
        if ( session.getAttribute("user") == null )
        {
            return "redirect:/";
        }

        model.addAttribute("project", projectRepository.getAllProjects());

        return "adminStart";
    }

    @GetMapping("/userStart")
    public String showUserStart(HttpSession session, Model model)
    {
        if ( session.getAttribute("user") == null )
        {
            return "redirect:/";
        }

        model.addAttribute("project", projectRepository.getAllProjects());

        return "userStart";
    }


    // This method is used to log out the user and invalidate the session.
    @GetMapping("/logout")
    public String logout(HttpSession session)
    {
        session.invalidate();
        return "redirect:/";
    }

    // This method is used to show the register page.
    @GetMapping("/register")
    public String registerUser()
    {
        return "register";
        //Ingen HTTPSession her fordi alle skal kunne lave et login.
    }

    // This method is used to register the user.
    @PostMapping("/register")
    public String registerUser(@RequestParam(value = "admin", defaultValue = "false") boolean admin,
                               @RequestParam("first_name") String firstname,
                               @RequestParam("last_name") String lastname,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password,
                               Model model,
                               HttpSession session)
    {
        //Check if user with mail already exists
        if ( !userRepository.checkIfUserExists(email) )
        {
            User user = new User(admin, firstname, lastname, email, password);
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


    // This method is used to show all users.
    @GetMapping("/addUser")
    public String showAddUser(HttpSession session)
    {
        if ( session.getAttribute("user") == null )
        {
            return "redirect:/";
        }

        return "addUser";
    }


    // This method is used to add a user.
    @PostMapping("/addUser")
    public String addUser(@RequestParam("user-admin") boolean newUserAdmin,
                          @RequestParam("user-firstname") String newFirstName,
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
        User newUser = new User(newUserAdmin, newFirstName, newLastName, newEmail, newPassword);

        //Gem ny User
        userRepository.addUser(newUser);

        //Tilbage til start så man kan logge ind
        return "start";
    }



    // This method is used to show the assignUser page.
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

    // This method is used to assign a user to a task.
    @PostMapping("/assignUser")
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

    // This method is used to show all users page with alle users.
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

    /* This method is used to show the admin page.
    Can be used for home button.
    When clicked, it will redirect to /adminStart if the user is an admin,
    otherwise it will redirect to /allTasks. */
    @GetMapping("/admin")
    public String admin(HttpSession session)
    {
        User user = (User) session.getAttribute("user");
        if ( user == null)
        {
            return "redirect:/";
        }
        else if (!user.isAdmin())
        {
            return "redirect:/allTasks";
        }
        else
        {
            return "adminStart";
        }
    }

    @PostMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") int id, HttpSession session)
    {
        if ( session.getAttribute("user") == null )
        {
            return "redirect:/";
        }

        User user = userRepository.getUserByID(id);

        userRepository.deleteUserByID(user);
        return "redirect:/allUsers";
    }

    @GetMapping("/editUser/{id}")
    public String showEditUser(@PathVariable("id") int id, HttpSession session, Model model)
    {
        if ( session.getAttribute("user") == null )
        {
            return "redirect:/";
        }

        User user = userRepository.getUserByID(id);

        model.addAttribute("user", user);
        return "editUser";
    }

    @PostMapping("/editUser")
    public String editUser(@RequestParam("user-id") int id,
                           @RequestParam("user-admin") boolean admin,
                           @RequestParam("user-firstname") String firstname,
                           @RequestParam("user-lastname") String lastname,
                           @RequestParam("user-email") String email,
                           @RequestParam("user-password") String password,
                           HttpSession session)
    {
        if ( session.getAttribute("user") == null )
        {
            return "redirect:/";
        }

        User user = new User(id, admin, firstname, lastname, email, password);

        userRepository.updateUser(user);

        return "redirect:/allUsers";
    }



}
