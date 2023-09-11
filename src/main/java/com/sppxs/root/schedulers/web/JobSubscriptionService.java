package com.sppxs.root.schedulers.web;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSubscriptionService {
    private final JobSubscriptionRepository jobSubscriptionRepository;

    public JobSubscriptionService(JobSubscriptionRepository jobSubscriptionRepository) {
        this.jobSubscriptionRepository = jobSubscriptionRepository;
    }

    public ResponseEntity<?> findAllJobs() {
        List<Job> jobs = jobSubscriptionRepository.findAll();
        return ResponseEntity.ok(jobs);
    }
}
