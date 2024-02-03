package com.sppxs.root.client.resources.users;

import com.sppxs.root.client.resources.jobs.MyAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangeUserRepository extends JpaRepository<ExchangeUser, Long> {
    @Query(value = "SELECT e.ACTION_ID FROM EXCHANGE_USER_ACTION e WHERE e.EXCHANGE_USER_ID= ?1", nativeQuery = true)
    List<MyAction> findMyActionsForSelectedExchangeUserSql(@Param("userId") Long userId);

    //SELECT * FROM EXCHANGE_USER_ACTION
    //MY_ACTION
    //EXCHANGE_USER


}
