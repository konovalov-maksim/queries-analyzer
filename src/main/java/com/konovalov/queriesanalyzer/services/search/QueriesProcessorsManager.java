package com.konovalov.queriesanalyzer.services.search;

import com.konovalov.queriesanalyzer.dao.QueriesDao;
import com.konovalov.queriesanalyzer.entities.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final GoogleQueriesProcessor googleQueriesProcessor;
    private final QueriesDao queriesDao;

    private final ScheduledExecutorService yandexSearchScheduler = Executors.newSingleThreadScheduledExecutor();
    private final ScheduledExecutorService googleSearchScheduler = Executors.newSingleThreadScheduledExecutor();
    private boolean isYandexSearchRunning = false;
    private boolean isGoogleSearchRunning = false;
    private ScheduledFuture<?> yandexScheduledFuture;
    private ScheduledFuture<?> googleScheduledFuture;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${search.requestDelay.yandex}")
    private long yandexRequestDelay;

    @Value("${search.requestDelay.google}")
    private long googleRequestDelay;

    public QueriesProcessorsManager(
            GoogleQueriesProcessor googleQueriesProcessor,
            YandexQueriesProcessor yandexQueriesProcessor,
            QueriesDao queriesDao) {
        this.googleQueriesProcessor = googleQueriesProcessor;
        this.yandexQueriesProcessor = yandexQueriesProcessor;
        this.queriesDao = queriesDao;
    }

//    @Scheduled(fixedRateString = "${search.queriesScanningDelay}")
    public synchronized void processUnprocessedQueries() {
        List<Query> googleUnprocessedQueries = queriesDao.findUnprocessedGoogleQueries();
        List<Query> yandexUnprocessedQueries = queriesDao.findUnprocessedYandexQueries();
        logger.info("Searching for unprocessed queries. Found for google: " + googleUnprocessedQueries.size()
                + "; Found for yandex: " + yandexUnprocessedQueries.size());
        if (!yandexUnprocessedQueries.isEmpty()) {
            yandexQueriesProcessor.addQueries(yandexUnprocessedQueries);
            startYandexSearch();
        }
        if (!googleUnprocessedQueries.isEmpty()) {
            googleQueriesProcessor.addQueries(googleUnprocessedQueries);
            startGoogleSearch();
        }
    }

    private synchronized void startYandexSearch() {
        if (isYandexSearchRunning) return;
        logger.info("Yandex search is started");
        yandexScheduledFuture = yandexSearchScheduler
                .scheduleAtFixedRate(yandexQueriesProcessor, 0, yandexRequestDelay, TimeUnit.MILLISECONDS);
        isYandexSearchRunning = true;
    }

    synchronized void stopYandexSearch() {
        if (!isYandexSearchRunning) return;
        logger.info("Yandex search is stopped");
        yandexScheduledFuture.cancel(false);
        isYandexSearchRunning = false;
    }

    private synchronized void startGoogleSearch() {
        if (isGoogleSearchRunning) return;
        logger.info("Google search is started");
        googleScheduledFuture = googleSearchScheduler
                .scheduleAtFixedRate(googleQueriesProcessor, 0, googleRequestDelay, TimeUnit.MILLISECONDS);
        isGoogleSearchRunning = true;
    }

    synchronized void stopGoogleSearch() {
        if (!isGoogleSearchRunning) return;
        logger.info("Google search is stopped");
        googleScheduledFuture.cancel(false);
        isGoogleSearchRunning = false;
    }
}
