package com.jobs.jobportal.model;
import com.jobs.jobportal.model.*;
import jakarta.persistence.*;

@Entity
@Table(name = "resumes")

public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    @Column(name = "file_data", columnDefinition = "bytea")
    private byte[] fileData;
    
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;


    @Column(name = "full_text", columnDefinition = "TEXT")
    private String fullText;

    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public String getUserEmail() {
        return user.getEmail();
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public String getUserName() {
        return user != null ? user.getUserName() : null;
    }

    public String getResumeText() {
        return fullText;
    }

}
