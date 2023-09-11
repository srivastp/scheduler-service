package com.sppxs.root;

import com.sppxs.root.client.resources.blog.Blog;
import com.sppxs.root.client.resources.blog.BlogRepository;
import com.sppxs.root.schedulers.job.EmailJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
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
    public CommandLineRunner run(
            @Qualifier("customEmailScheduler") Scheduler customEmailScheduler,
            BlogRepository blogRepository) {
        return (String[] args) -> {

            //Blog Watcher
            /*new Thread(
                    () -> savePosts(blogRepository)
            ).start();*/

            //Email Sender
            new Thread(
                    () -> executeEmailJobs(customEmailScheduler, 10)
            ).start();
        };
    }

    private void executeEmailJobs(Scheduler customEmailScheduler, int iterations) {
        Random rand = new Random();

        for (int i = 1; i < iterations; i++) {
            int y = rand.nextInt(3) + 3;
            Date after20Seconds = Date.from(LocalDateTime.now().plusSeconds(y).atZone(ZoneId.systemDefault()).toInstant());

            JobDetail jobDetail = JobBuilder
                    .newJob(EmailJob.class)
                    .usingJobData("email", "sppxs@foo.com")
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger().startAt(after20Seconds).build();

            try {
                customEmailScheduler.scheduleJob(jobDetail, trigger);
                System.out.println("Incoming new EmailJob request");
                Thread.sleep(10_000);
            } catch (SchedulerException e) {
                System.out.println("Issue running EmailJob");
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
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
