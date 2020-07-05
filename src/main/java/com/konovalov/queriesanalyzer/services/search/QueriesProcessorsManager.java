package com.konovalov.queriesanalyzer.services.search;

import com.konovalov.queriesanalyzer.dao.QueriesDao;
import com.konovalov.queriesanalyzer.entities.Query;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;

@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
@PropertySource("classpath:config.properties")
public class QueriesProcessorsManager {

    private final YandexQueriesProcessor yandexQueriesProcessor;
    private final QueriesDao queriesDao;

    private final ScheduledExecutorService yandexSearchScheduler = Executors.newSingleThreadScheduledExecutor();
    private boolean isYandexSearchRunning = false;
    private ScheduledFuture<?> yandexScheduledFuture;

    @Value("${search.requestDelay.yandex}")
    private long yandexRequestDelay;

    @Value("${search.requestDelay.google}")
    private long googleRequestDelay;

    public QueriesProcessorsManager(YandexQueriesProcessor yandexQueriesProcessor, QueriesDao queriesDao) {
        this.yandexQueriesProcessor = yandexQueriesProcessor;
        this.queriesDao = queriesDao;
    }

    @Scheduled(fixedRateString = "${search.queriesScanningDelay}")
    public void processUnprocessedQueries() {
        List<Query> googleUnprocessedQueries = queriesDao.findUnprocessedGoogleQueries();
        List<Query> yandexUnprocessedQueries = queriesDao.findUnprocessedYandexQueries();
        if (!yandexUnprocessedQueries.isEmpty()) {
            yandexQueriesProcessor.addQueries(yandexUnprocessedQueries);
            startYandexSearch();
        }
    }

    private synchronized void startYandexSearch() {
        if (isYandexSearchRunning) return;
        yandexScheduledFuture = yandexSearchScheduler
                .scheduleAtFixedRate(yandexQueriesProcessor, 0, yandexRequestDelay, TimeUnit.MILLISECONDS);
        isYandexSearchRunning = true;
    }

    private void stopYandexSearch() {
        if (!isYandexSearchRunning) return;
        yandexScheduledFuture.cancel(false);
        isYandexSearchRunning = false;
    }

    private void startGoogleSearch() {

    }

}
