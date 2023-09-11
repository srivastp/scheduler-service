package com.sppxs.root.schedulers.web;


import com.sppxs.root.schedulers.blogWatcher.BlogWatcherService;
import com.sppxs.root.schedulers.web.payload.SubscriptionCreationRequest;
import org.quartz.SchedulerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/jobs")
public class JobSubscriptionController {
    private final BlogWatcherService blogWatcherService;
    private final JobSubscriptionService jobSubscriptionService;

    public JobSubscriptionController(BlogWatcherService blogWatcherService, JobSubscriptionService jobSubscriptionService) {
        this.blogWatcherService = blogWatcherService;
        this.jobSubscriptionService = jobSubscriptionService;
    }

    @GetMapping("/")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> listJobs() {
        return jobSubscriptionService.findAllJobs();
    }

    @PostMapping("/subscribe")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> subscribe(@RequestBody final SubscriptionCreationRequest request) {
        return blogWatcherService.subscribe(request);
    }

    @DeleteMapping("/unsubscribe/{triggerKey}")
    @ResponseStatus(value = HttpStatus.OK)
    public void unsubscribe(@PathVariable String triggerKey) throws SchedulerException {
        blogWatcherService.unsubscribe(triggerKey);
    }
}

