package com.konovalov.queriesanalyzer.dao;

import com.konovalov.queriesanalyzer.entities.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QueriesDao extends PagingAndSortingRepository<Query, Long> {
    @org.springframework.data.jpa.repository.Query("select q from Query q where q.searchTask.id = :searchTaskId")
    List<Query> findBySearchTaskId(@Param("searchTaskId") long searchTaskId);
}
