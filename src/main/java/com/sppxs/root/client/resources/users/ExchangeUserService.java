package com.sppxs.root.client.resources.users;

import com.sppxs.root.client.resources.jobs.MyAction;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ExchangeUserService {
    private final ExchangeUserRepository exchangeUserRepository;

    public ExchangeUserService(ExchangeUserRepository exchangeUserRepository) {
        this.exchangeUserRepository = exchangeUserRepository;
    }

    public List<MyAction> findSubscriptionsByUserId(Long userId) {
        Optional<ExchangeUser> user = exchangeUserRepository.findById(userId);
        if (user.isPresent()) {
            return user.get().getMyActions();
        }
        return Collections.emptyList();
    }
}
