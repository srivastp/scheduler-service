package com.sppxs.root.schedulers.task;

import com.sppxs.root.schedulers.module.ExpiredTrigger;
import com.sppxs.root.schedulers.module.ExpiredTriggerRepository;
import com.sppxs.root.schedulers.blogWatcher.BlogWatcherService;
import jakarta.transaction.Transactional;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Component
public class PeriodicTask {
    private static final Logger logger = LoggerFactory.getLogger(PeriodicTask.class);
    private final ExpiredTriggerRepository expiredTriggerRepository;
    private final BlogWatcherService postWatcherService;

    public PeriodicTask(ExpiredTriggerRepository expiredTriggerRepository, BlogWatcherService blogWatcherService) {
        this.expiredTriggerRepository = expiredTriggerRepository;
        this.postWatcherService = blogWatcherService;
    }

    @Scheduled(cron = "${every-50-sec-cron}")
    @Transactional
    public void markTriggerForDeletionOnJobCompletion() {
        logger.info("Periodic task: {}; Thread: {}", new Date(), Thread.currentThread().getName());
        //ToDo - PS - Either delete records or find records with deleteDate = null;
        List<ExpiredTrigger> triggersMarkedForExpiration = expiredTriggerRepository.findAll();
        triggersMarkedForExpiration.stream().forEach(trigger -> {
            try {
                trigger.setDeleteDate(LocalDateTime.now(ZoneId.of("UTC")));
                expiredTriggerRepository.update(trigger.getDeleteDate(), trigger.getTriggerKey());
                postWatcherService.unsubscribe(trigger.getTriggerKey());
                logger.info("Periodic task deactivating trigger {} ", trigger.getTriggerKey());
            } catch (SchedulerException e) {
                logger.error("Periodic task failed to unsubscribe active trigger {}: {} ", trigger.getTriggerKey(), e);
                throw new RuntimeException(e);
            }
        });
    }
}
