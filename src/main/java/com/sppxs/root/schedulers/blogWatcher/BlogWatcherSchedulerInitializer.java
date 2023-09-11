package com.sppxs.root.schedulers.blogWatcher;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BlogWatcherSchedulerInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(BlogWatcherSchedulerInitializer.class);
    private final CustomBlogWatcherScheduler customBlogWatcherScheduler;

    public BlogWatcherSchedulerInitializer(CustomBlogWatcherScheduler customBlogWatcherScheduler) {
        this.customBlogWatcherScheduler = customBlogWatcherScheduler;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            customBlogWatcherScheduler.start();
            logger.info("Successfully initialized CustomPostWatcherScheduler");
        } catch (SchedulerException e) {
            logger.error("Unable to initialize CustomPostWatcherScheduler: {}", e);
        }
    }
}
