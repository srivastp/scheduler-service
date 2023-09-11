package com.sppxs.root.schedulers.blogWatcher;


import com.sppxs.root.schedulers.web.payload.SubscriptionCreationRequest;
import com.sppxs.root.schedulers.web.payload.SubscriptionCreationResponse;
import com.sppxs.root.schedulers.blogWatcher.jobDetail.trigger.BlogWatcherTriggerFactory;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class BlogWatcherService {
    private static final Logger logger = LoggerFactory.getLogger(BlogWatcherService.class);
    private final CustomBlogWatcherScheduler customBlogWatcherScheduler;
    private final BlogWatcherTriggerFactory blogWatcherTriggerFactory;

    public BlogWatcherService(
            CustomBlogWatcherScheduler customBlogWatcherScheduler,
            BlogWatcherTriggerFactory blogWatcherTriggerFactory) {
        this.customBlogWatcherScheduler = customBlogWatcherScheduler;
        this.blogWatcherTriggerFactory = blogWatcherTriggerFactory;
    }

    public ResponseEntity<?> subscribe(SubscriptionCreationRequest request) {
        Trigger trigger = blogWatcherTriggerFactory.generateTrigger(request.getBlogId(), request.getUsername());
        logger.info("%% Generated Trigger Key: {}", trigger.getKey());
        customBlogWatcherScheduler.addTrigger(trigger);
        SubscriptionCreationResponse response = new SubscriptionCreationResponse(
                true,
                trigger.getKey().getName(),
                trigger.getKey().getGroup(),
                MessageFormat.format("Watching Blog {1} for username {2}", request.getBlogId(), request.getUsername())
        );
        return ResponseEntity.ok(response);
    }

    public void unsubscribe(final String triggerKey) throws SchedulerException {
        customBlogWatcherScheduler.unsubscribe(triggerKey);
    }
}
