package com.konovalov.queriesanalyzer.services;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@PropertySource("classpath:config.properties")
public class HttpClientManager {

    @Value("${httpClient.connectionTimeout}")
    private int connectionTimeout;

    @Value("${httpClient.readTimeout}")
    private int readTimeout;

    @Value("${httpClient.writeTimeout}")
    private int writeTimeout;

    private final OkHttpClient basicClient = new OkHttpClient.Builder()
            .connectTimeout(connectionTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
            .writeTimeout(writeTimeout, TimeUnit.SECONDS)
            .build();

    //TODO реализовать ротацию прокси
    public synchronized OkHttpClient getNewClient() {
        return basicClient;
    }

    //TODO реализовать регистрацию падения соединения
    public synchronized OkHttpClient replaceBadClient() {
        return getNewClient();
    }
}
