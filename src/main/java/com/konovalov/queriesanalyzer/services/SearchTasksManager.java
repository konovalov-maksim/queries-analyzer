package com.konovalov.queriesanalyzer.services;

import com.konovalov.queriesanalyzer.entities.Query;
import com.konovalov.queriesanalyzer.entities.SearchTask;
import com.konovalov.queriesanalyzer.models.SearchTaskModel;
import com.konovalov.queriesanalyzer.dao.SearchTasksDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchTasksManager {

    private final SearchTasksDao searchTasksDao;

    @Autowired
    public SearchTasksManager(SearchTasksDao searchTasksDao) {
        this.searchTasksDao = searchTasksDao;
    }

    @Transactional
    public void createSearchTask(SearchTaskModel searchTaskModel) {
        SearchTask searchTask = new SearchTask();
        List<Query> queries = Arrays.stream(searchTaskModel.getQueries().split("\r\n"))
                .distinct()
                .map(Query::new)
                .collect(Collectors.toList());
        searchTask.addQueries(queries);
        searchTask.setDoGoogleSearch(searchTaskModel.getDoGoogleSearch());
        searchTask.setDoYandexSearch(searchTaskModel.getDoYandexSearch());
        searchTasksDao.save(searchTask);
    }

}
