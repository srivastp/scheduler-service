package com.sppxs.root.client.resources.users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "subscribed-action/users")
public class ExchangeUserController {
    private static final Logger logger = LoggerFactory.getLogger(ExchangeUserController.class);
    private final ExchangeUserService exchangeUserService;

    public ExchangeUserController(ExchangeUserService exchangeUserService) {
        this.exchangeUserService = exchangeUserService;
    }

    @GetMapping("/{userId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> findSubscriptionsByUserId(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                exchangeUserService.findSubscriptionsByUserId(userId)
        );
    }
}
