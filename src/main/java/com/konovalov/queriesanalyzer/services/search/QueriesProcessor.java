package com.konovalov.queriesanalyzer.services.search;

import com.konovalov.queriesanalyzer.dao.PagesDao;
import com.konovalov.queriesanalyzer.dao.QueriesDao;
import com.konovalov.queriesanalyzer.dao.SearchResultsDao;
import com.konovalov.queriesanalyzer.dao.SitesDao;
import com.konovalov.queriesanalyzer.entities.Page;
import com.konovalov.queriesanalyzer.entities.Query;
import com.konovalov.queriesanalyzer.entities.SearchResult;
import com.konovalov.queriesanalyzer.entities.Site;
import com.konovalov.queriesanalyzer.services.search.searchers.SearchListener;
import com.konovalov.queriesanalyzer.services.search.searchers.Searcher;
import okhttp3.HttpUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;


public abstract class QueriesProcessor implements Runnable, SearchListener {

    private final Deque<Searcher> searchers = new ConcurrentLinkedDeque<>();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private int threadsCount = 0 ;

    private final SitesDao sitesDao;
    private final SearchResultsDao searchResultsDao;
    private final PagesDao pagesDao;
    private final QueriesDao queriesDao;

    public QueriesProcessor(SitesDao sitesDao, SearchResultsDao searchResultsDao, PagesDao pagesDao, QueriesDao queriesDao) {
        this.sitesDao = sitesDao;
        this.searchResultsDao = searchResultsDao;
        this.pagesDao = pagesDao;
        this.queriesDao = queriesDao;
    }

    abstract Searcher createSearcher(Query query);

    abstract void setQueryStatusId(Query query, int statusId);

    abstract int getThreadsPoolSize();

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
            setQueryStatusId(query, 1);
            queriesDao.save(query);
            searchers.addLast(searcher);
        }
    }

    private boolean isThreadsPoolFull() {
        return threadsCount >= getThreadsPoolSize();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public synchronized void onSearchCompleted(Query query, SearchResult searchResult) {
        threadsCount--;
        logger.info(query + "processing complete. Threads: " + threadsCount);
        query.setSearchResult(searchResult);
        setQueryStatusId(query, 2);
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
        setQueryStatusId(query, 3);
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
