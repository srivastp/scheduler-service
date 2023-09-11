package com.sppxs.root.schedulers.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
public class SchedulingConfiguration implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

        threadPoolTaskScheduler.setPoolSize(2);
        threadPoolTaskScheduler.setThreadNamePrefix("quartz-job-pool-");
        threadPoolTaskScheduler.initialize();

        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
    }
}