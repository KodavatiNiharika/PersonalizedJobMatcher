package com.jobs.jobportal.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ats_scores")
public class AtsScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private Long jobId;

    @Column(nullable = false)
    private double atsScore;


    @Column(nullable = false) 
    private String email;

    // getters and setters
    public Long getId() { return id; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserEmail() { return email; }
    public void setUserEmail(String email) { this.email = email; }

    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }

    public double getAtsScore() { return atsScore; }
    public void setAtsScore(double atsScore) { this.atsScore = atsScore; }
}
