package com.konovalov.queriesanalyzer.dao;

import com.konovalov.queriesanalyzer.entities.SearchTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchTasksDao extends CrudRepository<SearchTask, Long> {
}
