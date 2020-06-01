package com.konovalov.queriesanalyzer.controllers;

import com.konovalov.queriesanalyzer.dao.QueriesDao;
import com.konovalov.queriesanalyzer.entities.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ResultsController {

    private final QueriesDao queriesDao;

    @Autowired
    public ResultsController(QueriesDao queriesDao) {
        this.queriesDao = queriesDao;
    }

    @GetMapping("/tasks/{searchTaskId}")
    public String showResults(
            @PathVariable("searchTaskId") Integer searchTaskId,
            Model model
    ) {
        List<Query> queries = queriesDao.findBySearchTaskId(searchTaskId);

        model.addAttribute("queries", queries);
        return "results";
    }
}
