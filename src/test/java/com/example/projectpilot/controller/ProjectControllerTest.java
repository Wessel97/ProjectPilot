package com.example.projectpilot.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.projectpilot.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
public class ProjectControllerTest {

    @InjectMocks
    private ProjectController projectController;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private Model model;

    private HttpSession session;
    private int projectId;

    @BeforeEach
    public void setup() {
        projectId = 1;
        session = new MockHttpSession();  // Initialize session
    }

    @Test
    public void testDeleteProjectWhenSessionIdIsNull() {
        // Set session id to null
        session.setAttribute("id", null);

        String viewName = projectController.deleteProject(projectId, session, model);

        assertEquals("redirect:/", viewName);
    }

    @Test
    public void testDeleteProjectSuccess() {
        // Set session id
        session.setAttribute("id", 1);

        when(projectRepository.deleteProjectById(projectId)).thenReturn(true);

        String viewName = projectController.deleteProject(projectId, session, model);

        assertEquals("redirect:/adminStart", viewName);
    }
}
