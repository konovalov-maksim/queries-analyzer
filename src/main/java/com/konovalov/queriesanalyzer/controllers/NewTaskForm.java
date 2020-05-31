package com.konovalov.queriesanalyzer.controllers;

import com.konovalov.queriesanalyzer.models.SearchTaskModel;
import com.konovalov.queriesanalyzer.services.SearchTasksManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class NewTaskForm {

    private final SearchTasksManager searchTasksManager;

    @Autowired
    public NewTaskForm(SearchTasksManager searchTasksManager) {
        this.searchTasksManager = searchTasksManager;
    }

    @GetMapping("/new-task")
    public String showNewTaskForm(Model model) {
        SearchTaskModel searchTaskModel = new SearchTaskModel();
        model.addAttribute("searchTaskModel", searchTaskModel);

        return "new-task";
    }

    @PostMapping("/new-task")
    public String processNewTaskForm(
            @ModelAttribute SearchTaskModel searchTaskModel,
            Model model) {
        searchTasksManager.createSearchTask(searchTaskModel);
        return "redirect:/tasks";
    }

}
