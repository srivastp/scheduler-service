package com.sppxs.root.schedulers.blogWatcher.jobDetail.trigger;

import com.sppxs.root.schedulers.blogWatcher.jobDetail.BlogWatcherJobDetail;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDataMap;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BlogWatcherTriggerFactory {
    private final BlogWatcherJobDetail postWatcherJobDetail;

    public BlogWatcherTriggerFactory(BlogWatcherJobDetail blogWatcherJobDetail) {
        this.postWatcherJobDetail = blogWatcherJobDetail;
    }

    public Trigger generateTrigger(final String blogId, final String username) {
        String keyString = "username" + "-" + blogId + "-" + UUID.randomUUID();
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("username", username);
        jobDataMap.put("blogId", blogId);
        return TriggerBuilder.newTrigger()
                .withIdentity(keyString)
                .forJob(postWatcherJobDetail.getJobDetail())
                .withSchedule(
                        CronScheduleBuilder.cronSchedule("*/30 * * * * ?"))
                .usingJobData(jobDataMap).
                build();
    }
}
