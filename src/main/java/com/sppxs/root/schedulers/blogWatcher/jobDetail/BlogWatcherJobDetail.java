package com.sppxs.root.schedulers.blogWatcher.jobDetail;

import com.sppxs.root.schedulers.blogWatcher.BlogWatcherJob;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.springframework.stereotype.Component;

@Component
public class BlogWatcherJobDetail {
    private static JobDetail jobDetail = JobBuilder.newJob(BlogWatcherJob.class)
            .withIdentity("blog-watcher-job", "DEFAULT")
            .storeDurably()
            .build();

    public JobDetail getJobDetail() {
        return jobDetail;
    }
}
