package com.sppxs.root.schedulers.job;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class EmailJob extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(EmailJob.class);

    @Override
    public void executeInternal(JobExecutionContext context) {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String param = dataMap.getString("email");

        logger.info("%% Executing EmailJob: Sending email to {} \n [Thread: {} || Pool: {}]",
                param,
                Thread.currentThread().getName(),
                Thread.currentThread().getThreadGroup().getName());
    }
}
