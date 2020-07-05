package com.konovalov.queriesanalyzer.dao;

import com.konovalov.queriesanalyzer.entities.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QueriesDao extends PagingAndSortingRepository<Query, Long> {
    @org.springframework.data.jpa.repository.Query(
            value = "select * from query q where q.search_task_id = :searchTaskId",
            countQuery = "select count(*) from query q where q.search_task_id = :searchTaskId",
            nativeQuery = true)
    Page<Query> findBySearchTaskId(@Param("searchTaskId") long searchTaskId, Pageable page);

    @org.springframework.data.jpa.repository.Query(value = "from Query q where q.searchTask.doGoogleSearch = true and q.googleStatusId in (0, 3)")
    List<Query> findUnprocessedGoogleQueries();

    @org.springframework.data.jpa.repository.Query(value = "from Query q where q.searchTask.doYandexSearch = true and q.yandexStatusId in (0, 3)")
    List<Query> findUnprocessedYandexQueries();
}
