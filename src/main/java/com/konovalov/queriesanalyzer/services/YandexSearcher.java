package com.konovalov.queriesanalyzer.services;

import com.konovalov.queriesanalyzer.entities.Query;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service(value = "yandexSearcher")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class YandexSearcher implements Searcher {

    private final OkHttpClient client;

    @Autowired
    public YandexSearcher(OkHttpClient client) {
        this.client = client;
    }

    @Override
    public void doSearch(Query query) {

    }

}
