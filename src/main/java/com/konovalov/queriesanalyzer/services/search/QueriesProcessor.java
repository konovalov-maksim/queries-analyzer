package com.konovalov.queriesanalyzer.services.search;

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

    private static final Object LOCK = new Object();
    private final Deque<Searcher> searchersQueue = new ConcurrentLinkedDeque<>();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private int threadsCount = 0 ;

    private final SitesDao sitesDao;
    private final SearchResultsDao searchResultsDao;
    private final QueriesDao queriesDao;
    private final QueriesProcessorsManager queriesProcessorsManager;

    public QueriesProcessor(
            SitesDao sitesDao,
            SearchResultsDao searchResultsDao,
            QueriesDao queriesDao,
            QueriesProcessorsManager queriesProcessorsManager) {
        this.sitesDao = sitesDao;
        this.searchResultsDao = searchResultsDao;
        this.queriesDao = queriesDao;
        this.queriesProcessorsManager = queriesProcessorsManager;
    }

    abstract Searcher createSearcher(Query query);

    abstract void setQueryStatusId(Query query, int statusId);

    abstract int getThreadsPoolSize();

    abstract void onAllQueriesProcessed(QueriesProcessorsManager queriesProcessorsManager);

    @Override
    public void run() {
        doSearch();
    }

    private synchronized void doSearch() {
        if (isThreadsPoolFull() || searchersQueue.isEmpty()) return;
        Searcher searcher = searchersQueue.pollFirst();
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
            searchersQueue.addLast(searcher);
            logger.info("Query " + query + " is added to queue");
        }
    }

    private boolean isThreadsPoolFull() {
        return threadsCount >= getThreadsPoolSize();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public synchronized void onSearchCompleted(Query query, SearchResult searchResult) {
        threadsCount--;
        logger.info(query + " processing complete. Threads: " + threadsCount);
        query.setSearchResult(searchResult);
        setQueryStatusId(query, 2);
        for (Page page : searchResult.getPages()) {
            bindToSite(page);
        }
        searchResultsDao.save(searchResult);
        queriesDao.save(query);
        if (searchersQueue.isEmpty()) onAllQueriesProcessed(queriesProcessorsManager);
    }

    @Override
    @Transactional
    public synchronized void onSearchFailed(Query query, Exception e) {
        threadsCount--;
        setQueryStatusId(query, 3);
        queriesDao.save(query);
        logger.error("Processing failed for query: " + query, e);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    void bindToSite(Page page) {
        synchronized (LOCK) {
            HttpUrl pageUrl = HttpUrl.parse(page.getUrl());
            String domain = pageUrl.host();
            Site site = sitesDao.findFirstByDomain(domain);
            if (site == null) {
                site = new Site(domain);
                sitesDao.save(site);
                logger.info("New site is added: " + site);
            }
            page.setSite(site);
            logger.info("Page " + page + " is bound to the site " + site);
        }
    }

}
