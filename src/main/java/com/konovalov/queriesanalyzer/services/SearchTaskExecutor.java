package com.konovalov.queriesanalyzer.services;

import com.konovalov.queriesanalyzer.dao.PagesDao;
import com.konovalov.queriesanalyzer.dao.SearchResultsDao;
import com.konovalov.queriesanalyzer.dao.SitesDao;
import com.konovalov.queriesanalyzer.entities.*;
import okhttp3.HttpUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ListIterator;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@PropertySource("classpath:config.properties")
public class SearchTaskExecutor implements Runnable {

    @Value("${searchTasks.requestDelay.yandex}")
    private int yandexRequestDelay;

    @Autowired
    public SearchTaskExecutor(SearchTask searchTask) {
        this.searchTask = searchTask;
    }

    private final SearchTask searchTask;
    private ApplicationContext context;
    private SitesDao sitesDao;
    private SearchResultsDao searchResultsDao;
    private PagesDao pagesDao;

    @Override
    public void run() {
        try {
            if (searchTask.getDoGoogleSearch()) doGoogleSearch();
            if (searchTask.getDoYandexSearch()) doYandexSearch();
        } catch (InterruptedException e) {
            //TODO logging
            e.printStackTrace();
        }
    }


    private void doYandexSearch() throws InterruptedException {
        ListIterator<Query> queriesIterator = searchTask.getQueries().listIterator();
        while (queriesIterator.hasNext()) {
            YandexSearcher searcher = context.getBean(YandexSearcher.class, queriesIterator.next());
            searcher.setSearchListener(searchListener);
            new Thread(searcher).start();
            if (queriesIterator.hasNext()) Thread.sleep(yandexRequestDelay);
        }
    }

    private void doGoogleSearch() {

    }

    private YandexSearcher.SearchListener searchListener = new YandexSearcher.SearchListener(){
        @Override
        public synchronized void onSearchCompleted(SearchResult searchResult) {
            processSearchResult(searchResult);
        }

        @Override
        public synchronized void onSearchFailed(Query query, Exception e) {

        }
    };

    @Transactional(propagation = Propagation.REQUIRED)
    void processSearchResult(SearchResult searchResult) {
        for (Page page : searchResult.getPages()) {
            bindToSite(page);
        }
        searchResultsDao.save(searchResult);
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

    @Autowired
    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    @Autowired
    public void setSitesDao(SitesDao sitesDao) {
        this.sitesDao = sitesDao;
    }

    @Autowired
    public void setSearchResultsDao(SearchResultsDao searchResultsDao) {
        this.searchResultsDao = searchResultsDao;
    }

    @Autowired
    public void setPagesDao(PagesDao pagesDao) {
        this.pagesDao = pagesDao;
    }

    public SearchTask getSearchTask() {
        return searchTask;
    }

}
