package com.konovalov.queriesanalyzer.dao;

import com.konovalov.queriesanalyzer.entities.SearchTask;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchTasksDao extends PagingAndSortingRepository<SearchTask, Long> {

}
