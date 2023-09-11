package com.sppxs.root.schedulers.module;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ExpiredTriggerRepository extends JpaRepository<ExpiredTrigger, String> {
    @Modifying
    @Query("update ExpiredTrigger u set u.deleteDate = ?1 where u.triggerKey = ?2")
    public void update(LocalDateTime deleteDate, String triggerKey );

}
