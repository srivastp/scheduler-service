package com.sppxs.root.client.resources.jobs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MyActionRepository extends JpaRepository<MyAction, Long> {

    @Query("select t from MyAction t where t.id = ?1")
    public MyAction findMyActionByIdSql(Long myActionId);
    public MyAction findMyActionByTitle(String title);
}
