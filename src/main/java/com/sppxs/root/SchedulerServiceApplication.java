package com.sppxs.root;

import com.sppxs.root.client.resources.blog.Blog;
import com.sppxs.root.client.resources.blog.BlogRepository;
import com.sppxs.root.client.resources.jobs.MyAction;
import com.sppxs.root.client.resources.jobs.MyActionRepository;
import com.sppxs.root.client.resources.users.ExchangeUser;
import com.sppxs.root.client.resources.users.ExchangeUserRepository;
import com.sppxs.root.client.resources.wip.*;
import com.sppxs.root.schedulers.job.EmailJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static com.sppxs.root.util.UtilityHelper.generateRandomString;

@SpringBootApplication
@EnableScheduling
//@EnableAsync
public class SchedulerServiceApplication {

    private final BookRepository bookRepository;
    private final ArticleRepository articleRepository;

    public SchedulerServiceApplication(BookRepository bookRepository,
                                       ArticleRepository articleRepository) {
        this.bookRepository = bookRepository;
        this.articleRepository = articleRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(SchedulerServiceApplication.class, args);
    }


    @Bean
    public CommandLineRunner run(
            @Qualifier("customEmailScheduler") Scheduler customEmailScheduler,
            BlogRepository blogRepository,
            MyActionRepository myActionRepository,
            ExchangeUserRepository exchangeUserRepository,
            PublicationRepository publicationRepository,
            AuthorRepository authorRepository) {
        return (String[] args) -> {

            //Create Blogs
            new Thread(
                    () -> createBlogs(blogRepository, 310)
            ).start();

            /*
            //Schedule Email Send
            new Thread(
                    () -> executeEmailJobs(customEmailScheduler, 20)
            ).start();

            //Create Jobs and Users
            setUpJobsAndUsers(myActionRepository, exchangeUserRepository);

            //Create Publications and Authors
            setUpPublicationsAndAuthors(publicationRepository, authorRepository);*/
        };
    }

    private void setUpPublicationsAndAuthors(PublicationRepository publicationRepository, AuthorRepository authorRepository) {

        Author t = new Author();
        t.setName("Paul Weber");
        authorRepository.save(t);

        Book b = new Book();
        b.setPages(12);
        b.setTitle("Shitty Life");
        b.setAuthors(List.of(t));
        bookRepository.save(b);


        t = new Author();
        t.setName("John Doe");
        authorRepository.save(t);

        Author m = new Author();
        m.setName("Jane Fonda");
        authorRepository.save(m);


        Article a = new Article();
        a.setUrl("https://www.foo.com");
        a.setAuthors(List.of(t, m));
        articleRepository.save(a);

        Optional<Author> author = authorRepository.findById(t.getId());
        author.get().getPublications().stream().forEach(x -> System.out.println(x.getTitle()));

        Optional<Article> article = articleRepository.findById(a.getId());
        article.get().getAuthors().stream().forEach(x -> System.out.println(x.getName()));

        Optional<Publication> publication = publicationRepository.findById(a.getId());
        article.get().getAuthors().stream().forEach(x -> System.out.println(x.getName()));

    }

    private void executeEmailJobs(Scheduler customEmailScheduler, int iterations) {
        Random rand = new Random();

        for (int i = 1; i < iterations; i++) {
            int y = rand.nextInt(3) + 3;
            y = 20;

            Date after20Seconds = Date.from(LocalDateTime.now().plusSeconds(y).atZone(ZoneId.systemDefault()).toInstant());

            JobDetail jobDetail = JobBuilder
                    .newJob(EmailJob.class)
                    .usingJobData("email", "sppxs@foo.com")
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger().startAt(after20Seconds).build();

            try {
                customEmailScheduler.scheduleJob(jobDetail, trigger);
                System.out.println("Incoming new EmailJob request");
                Thread.sleep(5_000);
                //Thread.sleep(10_000);
            } catch (SchedulerException e) {
                System.out.println("Issue running EmailJob");
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void createBlogs(BlogRepository blogRepository, int numberOfBlogs) {
        List blogType = Arrays.asList(
                "Technology",
                "Suspense",
                "Fiction",
                "Comedy",
                "Horror",
                "News",
                "Finance"
        );

        Random rand = new Random();
        for (int i = 1; i < numberOfBlogs; i++) {
            Blog blog = new Blog();
            blog.setTitle("Blog Title - " + i);
            blog.setBody(generateRandomString(rand));
            blog.setType((String) blogType.get(rand.nextInt(blogType.size())));
            Blog b = blogRepository.save(blog);
            //System.out.println(MessageFormat.format("%% Blog created with id {0}", b.getId()));
            try {
                TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(2_000, 5_000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void setUpJobsAndUsers(
            MyActionRepository myActionRepository,
            ExchangeUserRepository exchangeUserRepository) {

        List<String> postType = Arrays.asList(
                "Blog",
                "Operations",
                "Development"
        );

        MyAction jobMyAction1 = new MyAction();
        jobMyAction1.setTitle("job1");
        jobMyAction1.setCategory(postType.get(0));

        MyAction jobMyAction2 = new MyAction();
        jobMyAction2.setTitle("job2");
        jobMyAction2.setCategory(postType.get(1));
        jobMyAction2.setFrequency("0/30 * * * *");

        List jobs = List.of(
                jobMyAction1,
                jobMyAction2
        );

        myActionRepository.saveAll(Arrays.asList(jobMyAction1, jobMyAction2));

        ExchangeUser u = new ExchangeUser();
        u.setEmail("foo@barr.com");
        u.setMyActions(jobs);
        exchangeUserRepository.save(u);

        u = new ExchangeUser();
        u.setEmail("zeus@aha.com");
        u.setMyActions(List.of(jobMyAction1));
        exchangeUserRepository.save(u);
        //MyAction myAct = myActionRepository.findMyActionByIdSql(1L);
        //myAct.getExchangeUsers().stream().forEach(x -> System.out.println(x.getEmail()));

    }
}
