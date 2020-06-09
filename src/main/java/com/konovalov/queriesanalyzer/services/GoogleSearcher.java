package com.konovalov.queriesanalyzer.services;

import com.konovalov.queriesanalyzer.entities.Query;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service(value = "googleSearcher")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class GoogleSearcher implements Searcher {

    @Value("${searchTasks.requestDelay.google}")
    private int requestDelay;

    private final OkHttpClient client;


    @Autowired
    public GoogleSearcher(OkHttpClient client) {
        this.client = client;
    }

    @Override
    public void doSearch(Query query) {

    }

    public int getDelay() {
        return requestDelay;
    }
}
