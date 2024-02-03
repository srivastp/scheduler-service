package com.sppxs.root.schedulers.config;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class CustomSchedulerConfiguration {

    @Bean("customBlogWatcherSchedulerFactoryBean")
    public SchedulerFactoryBean customBlogWatcherSchedulerFactoryBean(DataSource dataSource) {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        Properties properties = new Properties();
        properties.setProperty("org.quartz.threadPool.threadNamePrefix", "sched-blog-watcher_worker");

        //ToDO: Try using Misfire Properties
        properties.setProperty("org.quartz.threadPool.threadCount", "3");
        //properties.setProperty("org.quartz.jobStore.misfireThreshold", "2"); //60000
        //properties.setProperty("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");

        properties.setProperty("org.quartz.jobStore.isClustered", "true");
        //properties.setProperty("org.quartz.jobStore.clusterCheckinInterval", "20000");
        //properties.setProperty("oorg.quartz.scheduler.instanceName", "my-instance");

        //properties.setProperty("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        //properties.setProperty("org.quartz.threadPool.threadPriority", "8");
        //properties.setProperty("org.quartz.jobStore.lockHandler.class", "org.quartz.impl.jdbcjobstore.StdRowLockSemaphore");
        //properties.setProperty("org.quartz.jobStore.lockHandler.maxRetry", "7");
        //properties.setProperty("org.quartz.jobStore.lockHandler.retryPeriod", "3000");

        factory.setQuartzProperties(properties);
        factory.setDataSource(dataSource);
        //factory.setWaitForJobsToCompleteOnShutdown(true);
        //factory.setTriggers();
        //factory.setJobFactory(springBeanJobFactory);
        //factory.setTriggerFactory(springBeanJobFactory);
        //factory.setApplicationContext(applicationContext);
        factory.setApplicationContextSchedulerContextKey("applicationContext");
        return factory;
    }
    @Bean("customEmailSchedulerFactoryBean")
    public SchedulerFactoryBean customEmailSchedulerFactoryBean(DataSource dataSource) {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        Properties properties = new Properties();
        properties.setProperty("org.quartz.threadPool.threadNamePrefix", "sched-email_worker");
        factory.setQuartzProperties(properties);
        factory.setDataSource(dataSource);
        return factory;
    }


    @Bean("customEmailSchedulerFactoryBean")
    public SchedulerFactoryBean customEmailSchedulerFactoryBean(DataSource dataSource) {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        Properties properties = new Properties();
        properties.setProperty("org.quartz.threadPool.threadNamePrefix", "sched-email_worker");
        //ToDO: Try using Misfire Properties
        //properties.setProperty("org.quartz.jobStore.misfireThreshold", "300000");
        factory.setQuartzProperties(properties);
        factory.setDataSource(dataSource);
        return factory;
    }


    @Bean("customEmailScheduler")
    public Scheduler customEmailScheduler(
            @Qualifier("customEmailSchedulerFactoryBean") SchedulerFactoryBean factory) throws SchedulerException {
        Scheduler scheduler = factory.getScheduler();
        scheduler.start();
        return scheduler;
    }

}