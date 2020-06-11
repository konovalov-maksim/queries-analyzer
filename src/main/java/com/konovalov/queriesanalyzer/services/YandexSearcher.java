package com.konovalov.queriesanalyzer.services;

import com.konovalov.queriesanalyzer.entities.Query;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service(value = "yandexSearcher")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class YandexSearcher implements Runnable{

    private  OkHttpClient client;
    private final Query query;

    public YandexSearcher(Query query) {
        this.query = query;
    }

    public Query getQuery() {
        return query;
    }

    @Autowired
    public void setClient(OkHttpClient client) {
        this.client = client;
    }

    @Override
    public void run() {
        System.out.println("Inside do search");

    }
}
