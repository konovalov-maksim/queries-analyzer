package com.konovalov.queriesanalyzer.services.search;

import com.konovalov.queriesanalyzer.dao.PagesDao;
import com.konovalov.queriesanalyzer.dao.QueriesDao;
import com.konovalov.queriesanalyzer.dao.SearchResultsDao;
import com.konovalov.queriesanalyzer.dao.SitesDao;
import com.konovalov.queriesanalyzer.entities.*;
import com.konovalov.queriesanalyzer.services.search.searchers.*;
import okhttp3.HttpUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
@PropertySource("classpath:config.properties")
public class YandexQueriesProcessor implements Runnable, SearchListener {

    private final Deque<Searcher> searchers = new ConcurrentLinkedDeque<>();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private int threadsCount = 0 ;

    @Value("${search.threadsPoolSize.yandex}")
    private int threadsPoolSize;

    private final ApplicationContext context;
    private final SitesDao sitesDao;
    private final SearchResultsDao searchResultsDao;
    private final PagesDao pagesDao;
    private final QueriesDao queriesDao;

    @Autowired
    public YandexQueriesProcessor(ApplicationContext context, SitesDao sitesDao, SearchResultsDao searchResultsDao, PagesDao pagesDao, QueriesDao queriesDao) {
        this.context = context;
        this.sitesDao = sitesDao;
        this.searchResultsDao = searchResultsDao;
        this.pagesDao = pagesDao;
        this.queriesDao = queriesDao;
    }

    @Override
    public void run() {
        doSearch();
    }

    private synchronized void doSearch() {
        if (isThreadsPoolFull() || searchers.isEmpty()) return;
        Searcher searcher = searchers.pollFirst();
        searcher.setSearchListener(this);
        threadsCount++;
        logger.info(searcher.getQuery() + " processing started. Threads: " + threadsCount);
        new Thread(searcher).start();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public synchronized void addQueries(List<Query> queries) {
        for (Query query : queries) {
            Searcher searcher = createSearcher(query);
            query.setYandexStatusId(1);
            queriesDao.save(query);
            searchers.addLast(searcher);
        }
    }

    private Searcher createSearcher(Query query) {
        return context.getBean(YandexSearcher.class, query);
    }

    private boolean isThreadsPoolFull() {
        return threadsCount >= threadsPoolSize;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public synchronized void onSearchCompleted(Query query, SearchResult searchResult) {
        threadsCount--;
        logger.info(query + "processing complete. Threads: " + threadsCount);
        query.setYandexSearchResult(searchResult);
        query.setYandexStatusId(2);
        for (Page page : searchResult.getPages()) {
            bindToSite(page);
        }
        searchResultsDao.save(searchResult);
        queriesDao.save(query);
    }

    @Override
    @Transactional
    public synchronized void onSearchFailed(Query query, Exception e) {
        threadsCount--;
        query.setYandexStatusId(3);
        queriesDao.save(query);
        //TODO логирование
    }


    @Transactional(propagation = Propagation.REQUIRED)
    void bindToSite(Page page) {
        HttpUrl pageUrl = HttpUrl.parse(page.getUrl());
        String domain = pageUrl.host();
        Site site = sitesDao.findFirstByDomain(domain);
        if (site == null) {
            site = new Site(domain);
            sitesDao.save(site);
        }
        page.setSite(site);
    }

}
