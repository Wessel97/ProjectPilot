package com.example.projectpilot.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.projectpilot.model.Department;
import com.example.projectpilot.repository.DepartmentRepository;
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
public class DepartmentControllerTest {

    @InjectMocks
    private DepartmentController departmentController;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private Model model;

    private HttpSession session;

    private String departmentName;

    @BeforeEach
    public void setup() {
        departmentName = "Test Department";
        session = new MockHttpSession();  // Initialize session
        session.setAttribute("projectId", 1);  // set projectId in session
    }

    @Test
    public void testAddDepartmentWhenExists() {
        when(departmentRepository.checkIfDepartmentExists(departmentName)).thenReturn(true);

        String viewName = departmentController.addDepartment(departmentName, model, session);

        verify(model).addAttribute("error", "Department already exists. Please enter a new name.");
        assertEquals("addDepartment", viewName);
    }

    @Test
    public void testAddDepartmentSuccess() {
        when(departmentRepository.checkIfDepartmentExists(departmentName)).thenReturn(false);
        when(departmentRepository.addDepartment(any(Department.class))).thenReturn(true);

        String viewName = departmentController.addDepartment(departmentName, model, session);

        assertEquals("redirect:/showProject/" + 1, viewName);
    }

    // Add more tests here for other scenarios like department creation failed
}
