package com.sppxs.root.schedulers.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(SimpleJob.class);

    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String param = dataMap.getString("param");
        /*System.out.println(MessageFormat.format("Executing Job: {0}; Param: {1}; Thread: {2}",
                getClass(), param, Thread.currentThread().getName()));*/
        logger.info("Executing Job: {}; Param: {}; Thread: {}  Pool: {}",
                getClass(),
                param,
                Thread.currentThread().getName(),
                Thread.currentThread().getThreadGroup().getName());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}