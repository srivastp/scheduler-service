package com.sppxs.root.client.resources.post;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BlogService {
    private static final Logger logger = LoggerFactory.getLogger(BlogService.class);
    private final BlogRepository blogRepository;

    public BlogService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    public List<Blog> findAll() {
        return blogRepository.findAll();
    }

    public Optional<Blog> findById(Long blogId) {
        return blogRepository.findById(blogId);
    }

    public Blog create(Blog blog) {
        return blogRepository.save(blog);
    }

    public List<Blog> findBlogsBetweenDates(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        logger.info("### StartTime {} | endTime: {}", startDateTime, endDateTime);
        return blogRepository.findAllBetweenDates(startDateTime, endDateTime);
    }

}
