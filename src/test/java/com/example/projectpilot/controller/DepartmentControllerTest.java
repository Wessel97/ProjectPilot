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
        session = new MockHttpSession();
        session.setAttribute("projectId", 1);
    }

    @Test
    public void testAddDepartmentWhenExists() {
        // Simulerer, at afdelingen allerede eksisterer i repository
        when(departmentRepository.checkIfDepartmentExists(departmentName)).thenReturn(true);

        // Kalder metoden til at tilføje afdeling og gemmer returværdien
        String viewName = departmentController.addDepartment(departmentName, model, session);

        // Verificerer, at fejlmeddelelsen blev tilføjet til modellen
        verify(model).addAttribute("error", "Department already exists. Please enter a new name.");

        // Verificerer, at visningen er korrekt
        assertEquals("addDepartment", viewName);
    }

    @Test
    public void testAddDepartmentSuccess() {
        // Simulerer, at afdelingen ikke eksisterer i repository
        when(departmentRepository.checkIfDepartmentExists(departmentName)).thenReturn(false);

        // Simulerer, at tilføjelse af afdeling i repository er vellykket
        when(departmentRepository.addDepartment(any(Department.class))).thenReturn(true);

        // Kalder metoden til at tilføje afdeling og gemmer returværdien
        String viewName = departmentController.addDepartment(departmentName, model, session);

        // Verificerer, at der er blevet foretaget en omdirigering til visningen af projektet med id 1
        assertEquals("redirect:/showProject/" + 1, viewName);
    }
}
