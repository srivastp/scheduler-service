package com.sppxs.root.schedulers.blogWatcher;


import com.sppxs.root.schedulers.blogWatcher.jobDetail.BlogWatcherJobDetail;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

@Component
public class CustomBlogWatcherScheduler {
    private static final Logger logger = LoggerFactory.getLogger(CustomBlogWatcherScheduler.class);
    private final Scheduler scheduler;
    private final BlogWatcherJobDetail blogWatcherJobDetail;

    public CustomBlogWatcherScheduler(
            @Qualifier("customBlogWatcherSchedulerFactoryBean") SchedulerFactoryBean schedulerFactoryBean,
            BlogWatcherJobDetail blogWatcherJobDetail) {
        this.scheduler = schedulerFactoryBean.getScheduler();
        this.blogWatcherJobDetail = blogWatcherJobDetail;
    }

    public void start() throws SchedulerException {
        this.scheduler.start();
        this.scheduler.addJob(blogWatcherJobDetail.getJobDetail(), false);
    }

    public void addTrigger(Trigger trigger) {
        try {
            this.scheduler.scheduleJob(trigger);
            logger.info("Successfully added trigger to scheduler with identity: {}", trigger.getKey());
        } catch (ObjectAlreadyExistsException exception) {
            logger.error("Trigger already added!");
            //throw new EmailAlreadyRegisteredException();
            throw new RuntimeException("Already Subscribed");
        } catch (SchedulerException e) {
            logger.error("Unable to add trigger {}", e);
            //throw new GenericServerException();
            throw new RuntimeException();
        }
    }

    public void unsubscribe(final String triggerKeyId) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(triggerKeyId, "DEFAULT");
        scheduler.unscheduleJob(triggerKey);
    }
}
