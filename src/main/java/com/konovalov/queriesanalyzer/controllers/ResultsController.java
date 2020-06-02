package com.konovalov.queriesanalyzer.controllers;

import com.konovalov.queriesanalyzer.dao.QueriesDao;
import com.konovalov.queriesanalyzer.entities.Query;
import com.konovalov.queriesanalyzer.models.PaginationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ResultsController {

    private final QueriesDao queriesDao;

    @Autowired
    public ResultsController(QueriesDao queriesDao) {
        this.queriesDao = queriesDao;
    }

    @GetMapping("/tasks/{searchTaskId}")
    public String showResults(
            @RequestParam(name = "page", defaultValue = "1") Integer pageNum,
            @PathVariable("searchTaskId") Integer searchTaskId,
            Model model
    ) {
        pageNum = pageNum - 1;
        if (pageNum < 0) pageNum = 0;
        Page<Query> queriesPage = queriesDao.findBySearchTaskId(searchTaskId, PageRequest.of(pageNum, 5, Sort.Direction.ASC,"id"));
        model.addAttribute("queries", queriesPage.getContent());

        PaginationModel pagination = new PaginationModel(queriesPage.getTotalPages(), pageNum);
        model.addAttribute("pagination", pagination);
        return "results";
    }
}
