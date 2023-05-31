package com.example.projectpilot.controller;

import com.example.projectpilot.model.Department;
import com.example.projectpilot.model.Task;
import com.example.projectpilot.repository.DepartmentRepository;
import com.example.projectpilot.repository.ProjectRepository;
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
    private final ProjectRepository projectRepository;

    public DepartmentController(DepartmentRepository departmentRepository, TaskRepository taskRepository, ProjectRepository projectRepository)
    {
        this.departmentRepository = departmentRepository;
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    // Viser add departmnets siden
    @GetMapping("/addDepartment")
    public String showAddDepartment(HttpSession session, Model model) {
        if (session.getAttribute("id") == null) {
            return "redirect:/";
        }

        int projectId = (int) session.getAttribute("projectId"); // Retrieve project ID from the session
        List<Department> departmentList = departmentRepository.getAllDepartmentsByProjectId(projectId);

        model.addAttribute("projectId", projectId);
        model.addAttribute("department", departmentList);
        model.addAttribute("newDepartment", new Department()); // this will be used to bind form data

        return "addDepartment";
    }

    // Poster ny department
    @PostMapping("/addDepartment")
    public String addDepartment(@RequestParam("department-name") String newName, HttpSession session)
    {
        if ( session.getAttribute("id") == null )
        {
            return "redirect:/";
        }
        Department newDepartment = new Department();
        newDepartment.setDepartmentName(newName);
        departmentRepository.addDepartment(newDepartment);
        return "redirect:/showProject";
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
            // Department not found
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

        session.setAttribute("departmentId", departmentId); // Store department ID in the session
        session.setAttribute("departmentName", departmentName); // Store department Name in the session

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


    @GetMapping("/showDepartmentUser/{id}")
    public String showDepartmentUser(@PathVariable("id") int departmentId, HttpSession session, Model model)
    {
        if (session.getAttribute("id") == null)
        {
            return "redirect:/";
        }

        Department department = departmentRepository.getDepartmentById(departmentId);
        if (department == null) {
            // Department not found
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

        session.setAttribute("departmentId", departmentId); // Store department ID in the session
        session.setAttribute("departmentName", departmentName); // Store department ID in the session

        return "showDepartmentUser";
    }
}
