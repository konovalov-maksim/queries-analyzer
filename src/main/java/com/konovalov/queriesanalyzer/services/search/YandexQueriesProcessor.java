package com.konovalov.queriesanalyzer.services.search;

import com.konovalov.queriesanalyzer.dao.PagesDao;
import com.konovalov.queriesanalyzer.dao.QueriesDao;
import com.konovalov.queriesanalyzer.dao.SearchResultsDao;
import com.konovalov.queriesanalyzer.dao.SitesDao;
import com.konovalov.queriesanalyzer.entities.*;
import com.konovalov.queriesanalyzer.services.search.searchers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
@PropertySource("classpath:config.properties")
public class YandexQueriesProcessor extends QueriesProcessor {

    @Value("${search.threadsPoolSize.yandex}")
    private int threadsPoolSize;

    private final ApplicationContext context;

    @Autowired
    public YandexQueriesProcessor(
            ApplicationContext context,
            SitesDao sitesDao,
            SearchResultsDao searchResultsDao,
            QueriesDao queriesDao,
            @Lazy QueriesProcessorsManager  queriesProcessorsManager) {
        super(sitesDao, searchResultsDao, queriesDao, queriesProcessorsManager);
        this.context = context;
    }

    @Override
    Searcher createSearcher(Query query) {
        return context.getBean(YandexSearcher.class, query);
    }

    @Override
    void setQueryStatusId(Query query, int statusId) {
        query.setYandexStatusId(statusId);
    }

    @Override
    int getThreadsPoolSize() {
        return threadsPoolSize;
    }

    @Override
    void onAllQueriesProcessed(QueriesProcessorsManager queriesProcessorsManager) {
        queriesProcessorsManager.stopYandexSearch();
    }

}
