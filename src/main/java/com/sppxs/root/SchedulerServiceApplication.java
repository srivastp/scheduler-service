package com.sppxs.root;

import com.sppxs.root.client.resources.blog.Blog;
import com.sppxs.root.client.resources.blog.BlogRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static com.sppxs.root.util.UtilityHelper.generateRandomString;

@SpringBootApplication
@EnableScheduling
//@EnableAsync
public class SchedulerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchedulerServiceApplication.class, args);
    }


    @Bean
    public CommandLineRunner run(BlogRepository blogRepository) {
        return (String[] args) -> {
            new Thread(
                    () -> savePosts(blogRepository)
            ).start();
        };
    }


    private void savePosts(BlogRepository blogRepository) {
        List postType = Arrays.asList(
                "Technology",
                "Suspense",
                "Fiction",
                "Comedy",
                "Horror",
                "News",
                "Finance"
        );

        Random rand = new Random();
        for (int i = 1; i < 501; i++) {
            Blog blog = new Blog();
            blog.setTitle("Blog Title - " + i);
            blog.setBody(generateRandomString(rand));
            blog.setType((String) postType.get(rand.nextInt(postType.size())));
            Blog b = blogRepository.save(blog);
            System.out.println(MessageFormat.format("%% Blog created with id {0}", b.getId()));
            try {
                TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(2_000, 5_000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
