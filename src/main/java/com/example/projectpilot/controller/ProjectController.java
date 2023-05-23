package com.example.projectpilot.controller;

import com.example.projectpilot.model.Department;
import com.example.projectpilot.model.Project;
import com.example.projectpilot.repository.DepartmentRepository;
import com.example.projectpilot.repository.ProjectRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProjectController
{
    private final ProjectRepository projectRepository;
    private final DepartmentRepository departmentRepository;

    Department newDepartment = new Department();

    @Autowired
    public ProjectController(ProjectRepository projectRepository, DepartmentRepository departmentRepository)
    {
        this.projectRepository = projectRepository;
        this.departmentRepository = departmentRepository;
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

        List<Department> departmentList = departmentRepository.getAllDepartmentsByProjectId(projectId);
        model.addAttribute("department", departmentList);
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
    public String addProject(@RequestParam("project-name") String newName, HttpSession session)
    {
        if ( session.getAttribute("user") == null )
        {
            return "redirect:/";
        }
        Project newProject = new Project();
        newProject.setProjectName(newName);
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
        Project updateProject = projectRepository.getProjectByID(projectId);
        model.addAttribute("project", updateProject);
        return "updateProject";
    }

    @PostMapping("/updateProject")
    public String updateProject(
            @RequestParam("projectID") int projectId,
            @RequestParam("projectName") String projectName,
            HttpSession session)

    {
        if ( session.getAttribute("user") == null )
        {
            return "redirect:/";
        }
        Project updateProject = new Project(projectId, projectName);
        projectRepository.updateProject(updateProject);
        return "redirect:/allProjects";
    }

    @PostMapping("/deleteProject")
    public String deleteProject(
            @RequestParam("projectId") int projectId,
            HttpSession session,
            Model model)
    {
        if (session.getAttribute("user") == null )
        {
            return "redirect:/";
        }
        if(projectRepository.deleteProjectById(projectId))
        {
            return "redirect:/allProjects";
        }
        else
        {
            model.addAttribute("error", "An error occurred while deleting the project. Please try again.");
            return "allProjects";
        }
    }
}
