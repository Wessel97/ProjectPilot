package com.example.projectpilot.controller;

import com.example.projectpilot.model.Department;
import com.example.projectpilot.model.Task;
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

    // Viser add departments siden
    @GetMapping("/addDepartment")
    public String showAddDepartment(HttpSession session) {
        if (session.getAttribute("id") == null) {
            return "redirect:/";
        }

        return "addDepartment";
    }

    // Poster ny department
    @PostMapping("/addDepartment")
    public String addDepartment( @RequestParam("department-name") String departmentName, Model model, HttpSession session)
    {
        int projectId = (int) session.getAttribute("projectId");
        if (departmentRepository.checkIfDepartmentExists(departmentName,projectId))
        {
            model.addAttribute("error", "Department already exists. Please enter a new name.");
            return "addDepartment";
        }
        else
        {
            Department newDepartment = new Department();
            newDepartment.setDepartmentName(departmentName);
            newDepartment.setProjectId(projectId);
            if(departmentRepository.addDepartment(newDepartment))
            {
                return "redirect:/showProject/" + projectId;            }
            else
            {
                model.addAttribute("error", "An error occurred while creating the department. Please try again.");
                return "addDepartment";
            }
        }
    }

    // Viser et department
    @GetMapping("/showDepartment/{id}")
    public String showDepartment(@PathVariable("id") int departmentId, HttpSession session, Model model)
    {
        if (session.getAttribute("id") == null)
        {
            return "redirect:/";
        }

        Department department = departmentRepository.getDepartmentById(departmentId);
        if (department == null) {
            return "redirect:/showProject";
        }

        List<Task> taskList = taskRepository.getAllTasksByDepartmentID(departmentId);
        model.addAttribute("task", taskList);
        model.addAttribute("department", department);

        String departmentName = departmentRepository.getDepartmentNameById(departmentId);

        int totalHours = taskRepository.totalHoursByDepartment(departmentId);
        model.addAttribute("totalHours", totalHours);

        int totalPrice = taskRepository.totalPriceByDepartment(departmentId);
        model.addAttribute("totalPrice", totalPrice);

        session.setAttribute("departmentId", departmentId);
        session.setAttribute("departmentName", departmentName);

        int sesh = 0;
        session.setAttribute("sesh", sesh);
        
        return "showDepartment";
    }

    // Viser update department siden
    @GetMapping("/updateDepartment/{id}")
    public String showUpdateDepartment(@PathVariable("id") int id, HttpSession session, Model model)
    {
        if ( session.getAttribute("id") == null)
        {
            return "redirect:/";
        }

        Department department = departmentRepository.getDepartmentById(id);
        model.addAttribute("department", department);

        return "updateDepartment";
    }

    // Poster update department
    @PostMapping("/updateDepartment")
    public String updateDepartment(
            @RequestParam("id") int departmentId,
            @RequestParam("departmentName") String departmentName,
            HttpSession session)
    {
        if ( session.getAttribute("id") == null )
        {
            return "redirect:/";
        }
        Department department = new Department(departmentId, departmentName);
        departmentRepository.updateDepartment(department);

        int projectId = (int) session.getAttribute("projectId");


        return "redirect:/showProject/" + projectId;
    }

    // Sletter department
    @PostMapping("/deleteDepartment")
    public String deleteDepartment(@RequestParam("id") int departmentId,
                                   HttpSession session,
                                   Model model)
    {
        if ( session.getAttribute("id") == null )
        {
            return "redirect:/";
        }

        if(departmentRepository.deleteDepartmentById(departmentId))
        {
            int projectId = (int) session.getAttribute("projectId");
            return "redirect:/showProject/" + projectId;
        }
        else
        {
            model.addAttribute("errorMessage", "An error occurred while deleting the department. Please try again.");
            int projectId = (int) session.getAttribute("projectId");
            return "redirect:/showProject/" + projectId;        }
    }

    // Viser department siden for user
    @GetMapping("/showDepartmentUser/{id}")
    public String showDepartmentUser(@PathVariable("id") int departmentId, HttpSession session, Model model)
    {
        if (session.getAttribute("id") == null)
        {
            return "redirect:/";
        }

        Department department = departmentRepository.getDepartmentById(departmentId);
        if (department == null) {
            return "redirect:/showProjectUser";
        }

        List<Task> taskList = taskRepository.getAllTasksByDepartmentID(departmentId);
        model.addAttribute("task", taskList);
        model.addAttribute("department", department);

        String departmentName = departmentRepository.getDepartmentNameById(departmentId);

        int totalHours = taskRepository.totalHoursByDepartment(departmentId);
        model.addAttribute("totalHours", totalHours);

        int totalPrice = taskRepository.totalPriceByDepartment(departmentId);
        model.addAttribute("totalPrice", totalPrice);

        session.setAttribute("departmentId", departmentId);
        session.setAttribute("departmentName", departmentName);

        int sesh = 0;
        session.setAttribute("sesh", sesh);

        return "showDepartmentUser";
    }
}
