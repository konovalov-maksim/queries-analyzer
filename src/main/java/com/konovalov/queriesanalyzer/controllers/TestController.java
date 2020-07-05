package com.konovalov.queriesanalyzer.controllers;

import com.konovalov.queriesanalyzer.dao.SearchTasksDao;
import com.konovalov.queriesanalyzer.services.search.QueriesProcessorsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final SearchTasksDao searchTasksDao;
    private final ApplicationContext context;
    private final QueriesProcessorsManager queriesProcessorsManager;

    @Autowired
    public TestController(SearchTasksDao searchTasksDao, ApplicationContext context, QueriesProcessorsManager queriesProcessorsManager) {
        this.searchTasksDao = searchTasksDao;
        this.context = context;
        this.queriesProcessorsManager = queriesProcessorsManager;
    }

    @GetMapping("/test")
    public String test() {
        queriesProcessorsManager.processUnprocessedQueries();
        return "done";
    }

}
