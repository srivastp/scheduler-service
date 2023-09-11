package com.sppxs.root.schedulers.config;

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


}