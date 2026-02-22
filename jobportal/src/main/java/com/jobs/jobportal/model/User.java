package com.jobs.jobportal.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", unique = true, nullable = false)
    private String userName;


    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    private int atsThreshold;

    public int getatsThreshold() {
        return atsThreshold;
    }
    public void setAtsThreshold(int atsThreshold) {
        this.atsThreshold = atsThreshold;
    }
    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
