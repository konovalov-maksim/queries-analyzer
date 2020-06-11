package com.konovalov.queriesanalyzer.services;

import com.konovalov.queriesanalyzer.entities.Query;
import com.konovalov.queriesanalyzer.entities.SearchTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ListIterator;

@Service
@Scope("prototype")
@PropertySource("classpath:config.properties")
public class SearchTaskExecutor implements Runnable{

    @Value("${searchTasks.requestDelay.yandex}")
    private int yandexRequestDelay;

    @Autowired
    public SearchTaskExecutor(SearchTask searchTask) {
        this.searchTask = searchTask;
    }

    private final SearchTask searchTask;
    private ApplicationContext context;

    @Override
    public void run() {
        try {
            if (searchTask.getDoGoogleSearch()) doGoogleSearch();
            if (searchTask.getDoYandexSearch()) doYandexSearch();
        } catch (InterruptedException e) {
            //TODO logging
            e.printStackTrace();
        }
    }


    private void doYandexSearch() throws InterruptedException {
        ListIterator<Query> queriesIterator = searchTask.getQueries().listIterator();
        while (queriesIterator.hasNext()) {
            YandexSearcher searcher = context.getBean(YandexSearcher.class, queriesIterator.next());
            new Thread(searcher).start();
            if (queriesIterator.hasNext()) Thread.sleep(yandexRequestDelay);
        }
    }

    private void doGoogleSearch() {

    }

    @Autowired
    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    public SearchTask getSearchTask() {
        return searchTask;
    }
}
