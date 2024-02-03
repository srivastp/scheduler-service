package com.sppxs.root.client.resources.jobs;

import com.sppxs.root.client.resources.blog.BlogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MyActionTask {
    private static final Logger logger = LoggerFactory.getLogger(MyActionTask.class);
    private final MyActionRepository myActionRepository;
    private final BlogRepository blogRepository;

    public MyActionTask(MyActionRepository myActionRepository, BlogRepository blogRepository) {
        this.myActionRepository = myActionRepository;
        this.blogRepository = blogRepository;
    }

    @Scheduled(cron = "${every-50-sec-cron}")
    public void executeThisTask() {
        int blogCount = blogRepository.findAll().size();
        logger.info("=====> Total Blog Count: {}. Sending blog count notifications to: ", blogCount);
        MyAction myAction = myActionRepository.findMyActionByTitle("job1");
        myAction.getExchangeUsers().stream()
                .forEach(
                        x -> logger.info(x.getEmail())
                );
    }
}
