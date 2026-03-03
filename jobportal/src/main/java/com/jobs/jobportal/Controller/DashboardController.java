package com.jobs.jobportal.controller;

import com.jobs.jobportal.repository.AtsScoreRepository;
import com.jobs.jobportal.repository.JobRepository;
import com.jobs.jobportal.repository.UserRepo;
import com.jobs.jobportal.model.Job;
import com.jobs.jobportal.model.User;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "http://localhost:3000")
public class DashboardController {

    private final AtsScoreRepository atsRepo;
    private final JobRepository jobRepo;
    private final UserRepo userRepo;

    public DashboardController(AtsScoreRepository atsRepo,
                               JobRepository jobRepo,
                               UserRepo userRepo) {
        this.atsRepo = atsRepo;
        this.jobRepo = jobRepo;
        this.userRepo = userRepo;
    }

    @GetMapping
    public List<Job> getJobs(org.springframework.security.core.Authentication authentication) {

        String email = authentication.getName();

        Long userId = userRepo.findByEmail(email)
                .orElseThrow()
                .getId();

        // Step 1: Get jobIds with ATS > 40
        List<Long> jobIds = atsRepo.findJobIdsWithHighAts(email);

        // Step 2: Fetch full Job objects
        return jobRepo.findAllById(jobIds);
    }
}
