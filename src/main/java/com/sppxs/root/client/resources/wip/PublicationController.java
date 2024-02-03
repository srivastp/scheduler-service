package com.sppxs.root.client.resources.wip;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "publications")
public class PublicationController {
    private static final Logger logger = LoggerFactory.getLogger(PublicationController.class);
    private final PublicationService publicationService;

    public PublicationController(PublicationService publicationService) {
        this.publicationService = publicationService;
    }

    @GetMapping("/")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(
                publicationService.findAll()
        );
    }
}
