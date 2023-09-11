package com.sppxs.root.client.resources.blog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

    //@Query(value = "SELECT * FROM POST WHERE CREATION_DATE >= :startDate AND CREATION_DATE <= :endDate", nativeQuery = true)
    @Query(value = "SELECT * FROM blog WHERE creation_date BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<Blog> findAllBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
