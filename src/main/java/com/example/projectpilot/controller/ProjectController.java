package com.example.projectpilot.controller;

import com.example.projectpilot.model.Department;
import com.example.projectpilot.model.Project;
import com.example.projectpilot.repository.DepartmentRepository;
import com.example.projectpilot.repository.ProjectRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProjectController
{
    private final ProjectRepository projectRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public ProjectController(ProjectRepository projectRepository, DepartmentRepository departmentRepository)
    {
        this.projectRepository = projectRepository;
        this.departmentRepository = departmentRepository;
    }

    // Viser et projekt
    @GetMapping("/showProject/{id}")
    public String showProject(@PathVariable("id") int projectId, HttpSession session, Model model)
    {
        if ( session.getAttribute("id") == null )
        {
            return "redirect:/";
        }

        List<Department> departmentList = departmentRepository.getAllDepartmentsByProjectId(projectId);
        session.setAttribute("projectName", projectRepository.getProjectByID(projectId).getProjectName()); // Store project name in the session
        session.setAttribute("projectId", projectId); // Store project ID in the session
        model.addAttribute("department", departmentList);
        return "showProject";
    }

    // Viser et projekt fra session
    @GetMapping("/showProjectFromSession")
    public String showProjectFromSession(HttpSession session)
    {
        try
        {
            int projectId = (int) session.getAttribute("projectId");
            return "redirect:/showProject/" + projectId;
        }
        catch(NullPointerException e)
        {
            return "redirect:/adminStart";
        }
    }

    // Viser add project siden
    @GetMapping("/addProject")
    public String showAddProject(HttpSession session, Model model)
    {
        if ( session.getAttribute("id") == null )
        {
            return "redirect:/";
        }
        model.addAttribute("project", new Project());
        return "addProject";
    }

    // Poster nyt project
    @PostMapping("/addProject")
    public String addProject(@RequestParam("project-name") String newName, HttpSession session, Model model)
    {
        if ( session.getAttribute("id") == null )
        {
            return "redirect:/";
        }
        if ( !projectRepository.checkIfProjectExists(newName) == false)
        {
            model.addAttribute("errorMessage", "Project already exists");
            return "/addProject";
        }
        Project newProject = new Project();
        newProject.setProjectName(newName);
        projectRepository.addProject(newProject);
        return "redirect:/adminStart";
    }

    // Viser update project siden
    @GetMapping("/updateProject/{id}")
    public String showUpdateProject(@PathVariable("id") int projectId, HttpSession session, Model model)
    {
        if ( session.getAttribute("id") == null )
        {
            return "redirect:/";
        }
        Project updateProject = projectRepository.getProjectByID(projectId);
        model.addAttribute("project", updateProject);
        return "updateProject";
    }

    // Poster update project
    @PostMapping("/updateProject")
    public String updateProject(
            @RequestParam("id") int projectId,
            @RequestParam("projectName") String projectName,
            HttpSession session)
    {
        if ( session.getAttribute("id") == null )
        {
            return "redirect:/";
        }

        Project updateProject = new Project(projectId, projectName);
        projectRepository.updateProject(updateProject);
        return "redirect:/adminStart";
    }

    // Sletter et project
    @PostMapping("/deleteProject")
    public String deleteProject(@RequestParam("projectId") int projectId,
            HttpSession session,
            Model model)
    {
        if ( session.getAttribute("id") == null )
        {
            return "redirect:/";
        }

        if ( projectRepository.deleteProjectById(projectId) )
        {
            return "redirect:/adminStart";
        }
        else
        {
            model.addAttribute("error", "An error occurred while deleting the project. Please try again.");
            return "adminStart";
        }
    }


    @GetMapping("/showProjectUser/{id}")
    public String showProjectUser(@PathVariable("id") int projectId, HttpSession session, Model model)
    {
        if ( session.getAttribute("id") == null )
        {
            return "redirect:/";
        }

        List<Department> departmentList = departmentRepository.getAllDepartmentsByProjectId(projectId);
        session.setAttribute("projectName", projectRepository.getProjectByID(projectId).getProjectName()); // Store project name in the session
        session.setAttribute("projectId", projectId); // Store project ID in the session
        model.addAttribute("department", departmentList);
        return "showProjectUser";
    }
}
