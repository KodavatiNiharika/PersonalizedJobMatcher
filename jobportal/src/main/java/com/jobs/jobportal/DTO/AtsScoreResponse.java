package com.jobs.jobportal.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AtsScoreResponse {
    private String userName;

    private String userEmail;
     @JsonProperty("score") 
    private double atsScore;

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }


    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }


    public double getAtsScore() { return atsScore; }
    public void setAtsScore(double atsScore) { this.atsScore = atsScore; }
}