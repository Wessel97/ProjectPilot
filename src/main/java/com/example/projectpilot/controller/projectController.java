package com.example.projectpilot.controller;

import com.example.projectpilot.model.Project;
import com.example.projectpilot.repository.ProjectRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class projectController
{
    private final ProjectRepository projectRepository;

    public projectController(ProjectRepository projectRepository)
    {
        this.projectRepository = projectRepository;
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
