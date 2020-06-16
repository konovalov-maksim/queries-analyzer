package com.konovalov.queriesanalyzer.dao;

import com.konovalov.queriesanalyzer.entities.SearchResult;
import org.springframework.data.repository.CrudRepository;

public interface SearchResultsDao extends CrudRepository<SearchResult, Long> {

}
