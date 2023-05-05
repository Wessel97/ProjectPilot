package com.example.projectpilot.model;

public class User {
    private int id;
    private String first_name;
    private String last_name;
    private String email;
    private String password;

    //constructor
    public User()
    {
    }

    //constructor
    public User(String first_name, String last_name, String email, String password)
    {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
    }

    public int getID()
    {
        return id;
    }

    public String getFirstName()
    {
        return first_name;
    }

    public String getLastName()
    {
        return last_name;
    }

    public String getEmail()
    {
        return email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setID(int id)
    {
        this.id = id;
    }

    public void setFirstName(String first_name)
    {
        this.first_name = first_name;
    }

    public void setLastName(String last_name)
    {
        this.last_name = last_name;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
