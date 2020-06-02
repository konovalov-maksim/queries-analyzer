package com.konovalov.queriesanalyzer.controllers;

import com.konovalov.queriesanalyzer.dao.SearchTasksDao;
import com.konovalov.queriesanalyzer.entities.SearchTask;
import com.konovalov.queriesanalyzer.models.PaginationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class TasksController {

    private final SearchTasksDao searchTasksDao;

    @Autowired
    public TasksController(SearchTasksDao searchTasksDao) {
        this.searchTasksDao = searchTasksDao;
    }

    @GetMapping("/tasks")
    public String showNewTaskForm(
            @RequestParam(name = "page", defaultValue = "1") Integer pageNum,
            Model model
    ) {
        pageNum = pageNum - 1;
        if (pageNum < 0) pageNum = 0;
        Page<SearchTask> tasksPage = searchTasksDao.findAll(PageRequest.of(pageNum, 20, Sort.Direction.DESC,"id"));
        model.addAttribute("tasksList", tasksPage.getContent());

        PaginationModel pagination = new PaginationModel(tasksPage.getTotalPages(), pageNum);
        model.addAttribute("pagination", pagination);
        return "tasks";
    }



}
