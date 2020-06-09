package com.konovalov.queriesanalyzer.services;

import com.konovalov.queriesanalyzer.entities.Query;
import com.konovalov.queriesanalyzer.entities.SearchTask;
import com.konovalov.queriesanalyzer.models.SearchTaskModel;
import com.konovalov.queriesanalyzer.dao.SearchTasksDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@PropertySource("classpath:config.properties")
public class SearchTasksManager {

    private final SearchTasksDao searchTasksDao;

    @Value("${searchTasks.maxThreads}")
    private int maxThreads;

    @Autowired
    public SearchTasksManager(SearchTasksDao searchTasksDao) {
        this.searchTasksDao = searchTasksDao;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void createSearchTask(SearchTaskModel searchTaskModel) {
        SearchTask searchTask = new SearchTask();
        List<Query> queries = Arrays.stream(searchTaskModel.getQueries().split("\r\n"))
                .distinct()
                .map(Query::new)
                .collect(Collectors.toList());
        searchTask.addQueries(queries);
        searchTask.setName(searchTaskModel.getName());
        searchTask.setDoGoogleSearch(searchTaskModel.getDoGoogleSearch());
        searchTask.setDoYandexSearch(searchTaskModel.getDoYandexSearch());
        searchTasksDao.save(searchTask);
    }

}
