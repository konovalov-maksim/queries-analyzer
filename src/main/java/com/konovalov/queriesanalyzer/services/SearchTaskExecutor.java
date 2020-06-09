package com.konovalov.queriesanalyzer.services;

import com.konovalov.queriesanalyzer.entities.Query;
import com.konovalov.queriesanalyzer.entities.SearchTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

@Service
@Scope("prototype")
@PropertySource("classpath:config.properties")
public class SearchTaskExecutor {

    @Value("${searchTasks.requestDelay.yandex}")
    private int yandexRequestDelay;

    private SearchTask searchTask;
    private final ApplicationContext context;

    private final Deque<Query> googleQueries = new ConcurrentLinkedDeque<>();

    @Autowired
    public SearchTaskExecutor(ApplicationContext context) {
        this.context = context;
    }

    public void execute(SearchTask searchTask) {
        if (this.searchTask != null) throw new IllegalStateException("Этот исполнитель задач уже был запущен ранее!");
        this.searchTask = searchTask;
        if (searchTask.getDoGoogleSearch()) doGoogleSearch(searchTask);
        if (searchTask.getDoYandexSearch()) doYandexSearch(searchTask);
    }

    private void doYandexSearch(SearchTask searchTask) {
        YandexSearcher searcher = context.getBean(YandexSearcher.class);
        Deque<Query> queries = new ConcurrentLinkedDeque<>();


    }

    private void doGoogleSearch(SearchTask searchTask) {

    }

    public SearchTask getSearchTask() {
        return searchTask;
    }
}
