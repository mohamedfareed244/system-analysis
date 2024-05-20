package com.example.models;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User  implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String name ;
    private String phonenumber;
    private String password;
    private int focusDuration = 25; // default value
    private int breakDuration = 5; // default value

    // getters and setters for the new fields
    public int getFocusDuration() {
        return focusDuration;
    }

    public void setFocusDuration(int focusDuration) {
        this.focusDuration = focusDuration;
    }

    public int getBreakDuration() {
        return breakDuration;
    }

    public void setBreakDuration(int breakDuration) {
        this.breakDuration = breakDuration;
    }
    

    // @OneToMany(cascade = CascadeType.ALL)
    // private List<Task> tasks;

    // public User() {
    //     tasks = new ArrayList<>();
    // }
    @OneToMany(mappedBy = "user")
    private List<Task> tasks;

    // Getters and setters
    
    public List<Task> getTasks() {
        
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
    // Setters and Getters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

   


    // public List<Task> getTasks() {
    //     return tasks;
    // }

    // public void setTasks(List<Task> tasks) {
    //     this.tasks = tasks;
    // }

    public static User signUp(String username2, String password2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'signUp'");
    }
}
