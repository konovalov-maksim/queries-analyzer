package com.konovalov.queriesanalyzer.services.search;

import com.konovalov.queriesanalyzer.dao.PagesDao;
import com.konovalov.queriesanalyzer.dao.QueriesDao;
import com.konovalov.queriesanalyzer.dao.SearchResultsDao;
import com.konovalov.queriesanalyzer.dao.SitesDao;
import com.konovalov.queriesanalyzer.entities.Query;
import com.konovalov.queriesanalyzer.services.search.searchers.GoogleSearcher;
import com.konovalov.queriesanalyzer.services.search.searchers.Searcher;
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
public class GoogleQueriesProcessor extends QueriesProcessor {

    @Value("${search.threadsPoolSize.google}")
    private int threadsPoolSize;

    private final ApplicationContext context;

    @Autowired
    public GoogleQueriesProcessor(
            ApplicationContext context,
            SitesDao sitesDao,
            SearchResultsDao searchResultsDao,
            QueriesDao queriesDao,
            @Lazy QueriesProcessorsManager queriesProcessorsManager) {
        super(sitesDao, searchResultsDao, queriesDao, queriesProcessorsManager);
        this.context = context;
    }

    @Override
    Searcher createSearcher(Query query) {
        return context.getBean(GoogleSearcher.class, query);
    }

    @Override
    void setQueryStatusId(Query query, int statusId) {
        query.setGoogleStatusId(statusId);
    }

    @Override
    int getThreadsPoolSize() {
        return threadsPoolSize;
    }

    @Override
    void onAllQueriesProcessed(QueriesProcessorsManager queriesProcessorsManager) {
        queriesProcessorsManager.stopGoogleSearch();
    }
}
