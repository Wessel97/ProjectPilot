package com.example.projectpilot.controller;

import com.example.projectpilot.model.Task;
import com.example.projectpilot.repository.DepartmentRepository;
import com.example.projectpilot.repository.TaskRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class DepartmentController
{
    private final DepartmentRepository departmentRepository;
    private final TaskRepository taskRepository;

    public DepartmentController(DepartmentRepository departmentRepository, TaskRepository taskRepository)
    {
        this.departmentRepository = departmentRepository;
        this.taskRepository = taskRepository;
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
    public String addDepartment(HttpSession session)
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
}
