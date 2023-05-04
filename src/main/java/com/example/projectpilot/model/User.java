package com.example.projectpilot.model;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String pw;

    //constructor
    public User()
    {
    }

    //constructor
    public User(String firstName, String lastName, String email, String pw)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.pw = pw;
    }

    public int getId()
    {
        return id;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getEmail()
    {
        return email;
    }

    public String pw()
    {
        return pw;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void pw(String pw)
    {
        this.pw = pw;
    }
}
