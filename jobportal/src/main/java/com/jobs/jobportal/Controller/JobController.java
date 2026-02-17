package com.jobs.jobportal.controller;

import com.jobs.jobportal.model.Job;
import com.jobs.jobportal.repository.JobRepository;
import com.jobs.jobportal.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "http://localhost:3000")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/create")
    public ResponseEntity<Job> createJob(@RequestBody Job job) {

        String loggedInUser = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Job savedJob = jobService.createJob(job, loggedInUser);

        return ResponseEntity.ok(savedJob);
    }

    @GetMapping
    public List<Job> getAllJobs() {
        return jobService.getAllJobs();
    }

    @GetMapping("/my-jobs")
    public List<Job> getMyJobs() {
        String loggedInUser = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return jobService.getJobsByUser(loggedInUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        Job job = jobService.getJobById(id);
        return ResponseEntity.ok(job);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Job> updateJob(
            @PathVariable Long id,
            @RequestBody Job updatedJob) {

        String loggedInUser = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Job savedJob = jobService.updateJob(id, updatedJob, loggedInUser);

        return ResponseEntity.ok(savedJob);
    }


    @DeleteMapping("/{id}")
public ResponseEntity<?> deleteJob(@PathVariable Long id) {

    String loggedInUser = SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getName();

    jobService.deleteJob(id, loggedInUser);

    return ResponseEntity.ok("Deleted successfully");
}

}
