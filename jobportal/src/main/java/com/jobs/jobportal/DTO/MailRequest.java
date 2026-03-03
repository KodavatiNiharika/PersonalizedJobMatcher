package com.jobs.jobportal.DTO;


import java.util.List;

public class MailRequest {

    private Long jobId;
    private List<String> selectedUsers;

    public MailRequest() {}

    public MailRequest(Long jobId, List<String> selectedUsers) {
        this.jobId = jobId;
        this.selectedUsers = selectedUsers;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public List<String> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(List<String> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }
}