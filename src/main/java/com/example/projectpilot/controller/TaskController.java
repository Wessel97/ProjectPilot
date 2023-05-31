package com.example.projectpilot.controller;

import com.example.projectpilot.model.Task;
import com.example.projectpilot.repository.TaskRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Controller
public class TaskController
{
    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository)
    {
        this.taskRepository = taskRepository;
    }

    // Viser add task siden
    @GetMapping("/addTask")
    public String showAddTask(HttpSession session, Model model)
    {
        if ( session.getAttribute("id") == null )
        {
            return "redirect:/";
        }
        model.addAttribute("task", new Task());
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
                          HttpSession session)
    {
        if ( session.getAttribute("id") == null )
        {
            return "redirect:/";
        }

        int departmentId = (int) session.getAttribute("departmentId");
        String departmentName = (String) session.getAttribute("departmentName");

        Date sqlStartDate = Date.valueOf(newStartDate);
        Date sqlEndDate = Date.valueOf(newEndDate);

        Task newTask = new Task();
        newTask.setDepartment_id(departmentId);
        newTask.setTitle(newTitle);
        newTask.setDescription(newDescription);
        newTask.setNote(newNote);
        newTask.setHours(newHours);
        newTask.setStart_Date(sqlStartDate);
        newTask.setEnd_Date(sqlEndDate);
        newTask.setStatus(newStatus);
        newTask.setDepartment(departmentName);

        taskRepository.addTask(newTask);

        return "redirect:/showDepartment/" + departmentId;
    }

    // Viser update task siden
    @GetMapping("/updateTask/{id}")
    public String showUpdateTask(@PathVariable("id") int updateId, HttpSession session, Model model)
    {
        if ( session.getAttribute("id") == null )
        {
            return "redirect:/";
        }

        Task updateTask = taskRepository.getTaskByTaskId(updateId);

        model.addAttribute("task", updateTask);

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
                             @RequestParam(value = "task-flag", defaultValue = "false") boolean updateFlag,
                             @RequestParam("task-start_date") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate updateStartDate,
                             @RequestParam("task-end_date") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate updateEndDate,
                             @RequestParam("task-status") String updateStatus,
                             @RequestParam("task-department") String updateDepartment,
                             HttpSession session)
    {
        if ( session.getAttribute("id") == null )
        {
            return "redirect:/";
        }

        Date sqlStartDate = Date.valueOf(updateStartDate);
        Date sqlEndDate = Date.valueOf(updateEndDate);

        Task updateTask = new Task(updateTaskId, updateUserId, updateTitle, updateDescription, updateNote, updateHours, updatePayRate, updateFlag, sqlStartDate, sqlEndDate, updateStatus, updateDepartment);

        taskRepository.updateTask(updateTask);

        int check = (int) session.getAttribute("sesh");

        if(check == 0) {
            int departmentId = (int) session.getAttribute("departmentId");
            return "redirect:/showDepartment/" + departmentId;
        }
        else {
            int sesh = 0;
            session.setAttribute("sesh", sesh);
            return "redirect:/userTasks";
        }
    }


    // Sletter en task
    @PostMapping("/deleteTask/{id}")
    public String deleteTask(@PathVariable("id") int taskId, HttpSession session)
    {
        if ( session.getAttribute("id") == null )
        {
            return "redirect:/";
        }
        taskRepository.deleteTaskByID(taskId);

        int departmentId = (int) session.getAttribute("departmentId");
        return "redirect:/showDepartment/" + departmentId;
    }

    // Viser alle tasks for en User
    @GetMapping("/userTasks")
    public String showUserTasks(HttpSession session, Model model, @RequestParam(required = false) String sortingParameter)
    {
        if ( session.getAttribute("id") == null )
        {
            return "redirect:/";
        }
        int userId = (int) session.getAttribute("id");
        List<Task> taskList;

        if ( sortingParameter != null )
        {
            taskList = taskRepository.getAllTasksByUserID(userId, sortingParameter);
        }
        else
        {
            taskList = taskRepository.getAllTasksByUserID(userId, null);
        }

        int sesh = 1;

        session.setAttribute("sesh", sesh);

        model.addAttribute("tasks", taskList);
        return "userTasks";
    }

    // Viser alle tasks
    @GetMapping("/allTasks")
    public String showAllTasks(HttpSession session, Model model, @RequestParam(required = false) String sortingParameter)
    {
        if ( session.getAttribute("id") == null )
        {
            return "redirect:/";
        }
        int projectId = (int) session.getAttribute("projectId");
        List<Task> taskList;

        if ( sortingParameter != null )
        {
            taskList = taskRepository.getAllTasksByProjectId(projectId, sortingParameter);
        }
        else
        {
            taskList = taskRepository.getAllTasksByProjectId(projectId, null);
        }

        model.addAttribute("tasks", taskList);
        return "allTasks";
    }

}
