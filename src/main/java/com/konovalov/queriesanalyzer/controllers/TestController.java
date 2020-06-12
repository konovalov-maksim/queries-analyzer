package com.konovalov.queriesanalyzer.controllers;

import com.konovalov.queriesanalyzer.dao.SearchTasksDao;
import com.konovalov.queriesanalyzer.entities.SearchTask;
import com.konovalov.queriesanalyzer.services.SearchTaskExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final SearchTasksDao searchTasksDao;
    private final ApplicationContext context;

    @Autowired
    public TestController(SearchTasksDao searchTasksDao, ApplicationContext context) {
        this.searchTasksDao = searchTasksDao;
        this.context = context;
    }

    @GetMapping("/test")
    public String test() {
        SearchTask searchTask = searchTasksDao.findById(36L).get();
        SearchTaskExecutor executor = context.getBean(SearchTaskExecutor.class, searchTask);
        new Thread(executor).start();
        return "done";
    }

}
