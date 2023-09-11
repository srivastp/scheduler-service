package com.sppxs.root.schedulers.blogWatcher;

import com.sppxs.root.client.resources.post.Blog;
import com.sppxs.root.client.resources.post.BlogService;
import com.sppxs.root.schedulers.module.ExpiredTrigger;
import com.sppxs.root.schedulers.module.ExpiredTriggerRepository;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

public class BlogWatcherJob extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(BlogWatcherJob.class);

    @Override
    public void executeInternal(JobExecutionContext context) {
        ApplicationContext applicationContext;
        try {
            applicationContext = (ApplicationContext)
                    context.getScheduler().getContext().get("applicationContext");
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }

        //JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        JobDataMap dataMap = context.getTrigger().getJobDataMap();
        String username = dataMap.getString("username");
        Long blogId = Long.valueOf(dataMap.getString("blogId"));

        // ********************
        LocalDateTime prevTriggerRunTime = context.getTrigger().getPreviousFireTime().toInstant()
                .atZone(ZoneId.of("UTC"))
                .toLocalDateTime(); //.minusMinutes(1);

        LocalDateTime nextTriggerRunTime = context.getTrigger().getNextFireTime().toInstant()
                .atZone(ZoneId.of("UTC"))
                .toLocalDateTime();

        BlogService blogService = applicationContext.getBean(BlogService.class);
        int totalPosts = blogService.findAll().size();
        int totalNewPosts = blogService.findBlogsBetweenDates(prevTriggerRunTime, nextTriggerRunTime).size();

        Optional<Blog> post = blogService.findById(blogId);

        if (post.isEmpty()) {
            logger.info("BlogId {} not found", blogId);
        } else {
            logger.info("BlogId {} found for notifying user {}. Reference Trigger key: {} ",
                    blogId,
                    username,
                    context.getTrigger().getKey());
            ExpiredTrigger aTrigger = new ExpiredTrigger();
            aTrigger.setTriggerKey(context.getTrigger().getKey().getName());
            ExpiredTriggerRepository expiredTriggerRepository = applicationContext.getBean(ExpiredTriggerRepository.class);
            expiredTriggerRepository.save(aTrigger);
        }

        logger.info("\n=============\n- Previous Run time: {} \n- Next Run time: {} ",
                prevTriggerRunTime,
                nextTriggerRunTime);
        logger.info("\n=====\n- Job: {} \n- # of Posts: {} \n- # of New Posts: {} \n- Thread: {}  \n- Pool: {}",
                getClass(),
                totalPosts,
                totalNewPosts,
                Thread.currentThread().getName(),
                Thread.currentThread().getThreadGroup().getName() +
                        "\n=============\n"
        );
    }
}
