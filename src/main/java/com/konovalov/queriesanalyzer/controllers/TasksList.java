package com.konovalov.queriesanalyzer.controllers;

import com.konovalov.queriesanalyzer.entities.SearchTask;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class TasksList {

    @GetMapping("/tasks-list")
    public String showNewTaskForm(Model model) {

        return "tasks-list";
    }



}
