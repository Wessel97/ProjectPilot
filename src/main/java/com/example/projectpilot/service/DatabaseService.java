package com.example.projectpilot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class DatabaseService
{

    private final DataSource dataSource;

    public DatabaseService(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }
    
    public Connection getConnection() throws SQLException
    {
        return dataSource.getConnection();
    }
}
