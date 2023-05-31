package com.example.projectpilot.repository;

import com.example.projectpilot.model.Project;
import com.example.projectpilot.service.DatabaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


class ProjectRepositoryTest
{
    @Mock
    private DatabaseService databaseService;
    @Mock
    private ResultSet resultSet;

    private ProjectRepository projectRepository;

    // Denne test tester at metoden "getProject" i ProjectRepository interagere
    @BeforeEach
    public void setUp() throws SQLException
    {
        // Vi bruger MockitoAnnotations.openMocks(this) til at initialisere alle @Mock objekter.
        MockitoAnnotations.openMocks(this);
        // Initialiser the mock ResultSet
        projectRepository = new ProjectRepository(databaseService);

        // Sæt op mock for databaseService and connection
        when(resultSet.getInt(1)).thenReturn(1);
        when(resultSet.getString(2)).thenReturn("Sample Project");
    }

    // I @Test verifyer vi at getConnection() og prepareStatement() og executeUpdate() bliver kaldt 1 gang.
    @Test
    public void testGetProject() throws SQLException {
        Project project = projectRepository.getProject(resultSet);
        // Check at project er blevet initialiseret korrekt
        assertEquals(1, project.getId());
        assertEquals("Sample Project", project.getProjectName());
    }
}
