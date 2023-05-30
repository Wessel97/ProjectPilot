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

    @BeforeEach
    public void setUp() throws SQLException
    {
        MockitoAnnotations.openMocks(this);
        projectRepository = new ProjectRepository(databaseService);

        when(resultSet.getInt(1)).thenReturn(1);
        when(resultSet.getString(2)).thenReturn("Sample Project");
    }

    @Test
    public void testGetProject() throws SQLException {
        Project project = projectRepository.getProject(resultSet);

        assertEquals(1, project.getId());
        assertEquals("Sample Project", project.getProjectName());
    }
}
