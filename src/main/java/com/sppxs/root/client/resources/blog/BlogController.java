package com.sppxs.root.client.resources.blog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/blogs")
public class BlogController {
    private static final Logger logger = LoggerFactory.getLogger(BlogController.class);
    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(
                blogService.findAll()
        );
    }

    @PostMapping("/")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> create(@RequestBody final Blog request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                blogService.create(request)
        );
    }

    @PostMapping("/search")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> findAllPostsBetweenDates(
            @RequestBody DateSearcherDto dateSearcherDto
    ) {
        logger.info("%%%=== StartTime (UTC): {} || EndTime (UTC): {}", dateSearcherDto.getStartDate(), dateSearcherDto.getEndDate());
        return ResponseEntity.status(HttpStatus.OK).body(
                blogService.findBlogsBetweenDates(dateSearcherDto.getStartDate(), dateSearcherDto.getEndDate())
        );
    }
}
