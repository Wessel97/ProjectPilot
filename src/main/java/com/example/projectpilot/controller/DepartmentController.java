package com.example.projectpilot.controller;

import com.example.projectpilot.model.Department;
import com.example.projectpilot.model.Project;
import com.example.projectpilot.model.Task;
import com.example.projectpilot.model.User;
import com.example.projectpilot.repository.DepartmentRepository;
import com.example.projectpilot.repository.TaskRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String showAllDepartments(HttpSession session, Model model)
    {
        if(session.getAttribute("user") == null )
        {
            return "redirect:/";
        }
        model.addAttribute("department", departmentRepository.getAllDepartments());

        return "allDepartments";
    }

    @GetMapping("/addDepartment")
    public String ShowAddDepartment(HttpSession session, Model model)
    {
        if ( session.getAttribute("user") == null )
        {
            return "redirect:/";
        }

        Department newDepartment = new Department(); // assuming Department is a class that has a property departmentName
        model.addAttribute("department", newDepartment); // this will be used to bind form data

        return "addDepartment";
    }

    @PostMapping("/addDepartment")
    public String addDepartment(@RequestParam("departmentName") String departmentName, Model model)
    {
        // check if department exists
        if (departmentRepository.checkIfDepartmentExists(departmentName))
        {
            model.addAttribute("error", "Department already exists. Please enter a new name.");
            return "addDepartment";
        }
        else
        {
            Department newDepartment = new Department();
            newDepartment.setDepartmentName(departmentName);
            if(departmentRepository.addDepartment(newDepartment))
            {
                return "redirect:/departmentList"; // or whatever your success page is
            }
            else
            {
                model.addAttribute("error", "An error occurred while creating the department. Please try again.");
                return "addDepartment";
            }
        }
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

    @GetMapping("/allDepartments")
    public String showDepartmentsByProject(HttpSession session, Model model)
    {
        if (session.getAttribute("user") == null)
        {
            return "redirect:/";
        }
        User user = (User) session.getAttribute("user");
        List<Task> taskList = taskRepository.getAllTasksByUserID(user.getId());
        model.addAttribute("task", taskList);
        return "userTasks";
    }

    @GetMapping("/updateDepartment/{id}")
    public String showEditDepartment(@PathVariable("id") int departmentId, HttpSession session, Model model)
    {
        if ( session.getAttribute("user") == null)
        {
            return "redirect:/";
        }

        Department department = departmentRepository.getDepartmentById(departmentId);
        model.addAttribute("department", department);

        return "updateDepartment";
    }

    @PostMapping("/updateDepartment")
    public String updateDepartment(
            @RequestParam("departmentId") String departmentName,
            @RequestParam("departmentName") int departmentId,
            HttpSession session)
    {
        if ( session.getAttribute("user") == null )
        {
            return "redirect:/";
        }
        Department updateDepartment = new Department(departmentName, departmentId);
        DepartmentRepository.updateDepartment(updateDepartment);
        return "redirect:/allDepartments";
    }

    @PostMapping("/deleteDepartment")
    public String deleteDepartment(@RequestParam("departmentId") int departmentId, Model model)
    {
        if(departmentRepository.deleteDepartmentById(departmentId))
        {
            return "redirect:/allDepartments";
        }
        else
        {
            model.addAttribute("error", "An error occurred while deleting the department. Please try again.");
            return "allDepartments";
        }
    }

}
