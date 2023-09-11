package com.sppxs.root.schedulers.web;


import com.sppxs.root.schedulers.blogWatcher.BlogWatcherService;
import com.sppxs.root.schedulers.web.payload.SubscriptionCreationRequest;
import org.quartz.SchedulerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/jobs")
public class PostSubscriptionController {
    private final BlogWatcherService blogWatcherService;

    public PostSubscriptionController(BlogWatcherService blogWatcherService) {
        this.blogWatcherService = blogWatcherService;
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

