package com.sppxs.root.client.resources.jobs;

import com.sppxs.root.client.resources.blog.BlogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Component
public class CleanBlogActionTask {
    private static final Logger logger = LoggerFactory.getLogger(CleanBlogActionTask.class);
    private final BlogRepository blogRepository;
    private final int MAX_BLOG_COUNT = 50;

    public CleanBlogActionTask(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @Scheduled(cron = "${every-50-sec-cron}")
    public void executeThisTask() {
        int blogCount = blogRepository.findAll().size();
        if (blogCount > MAX_BLOG_COUNT) {
            logger.info("===> Max Blog Count of {} reached. Archiving old blogs: ", MAX_BLOG_COUNT);

            blogRepository.deleteAllById(
                    LongStream.range(1, 50)
                            .boxed()
                            .collect(Collectors.toList()));
        }
    }
}
