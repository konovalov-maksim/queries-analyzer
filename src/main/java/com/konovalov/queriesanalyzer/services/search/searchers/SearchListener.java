package com.konovalov.queriesanalyzer.services.search.searchers;

import com.konovalov.queriesanalyzer.entities.Query;
import com.konovalov.queriesanalyzer.entities.SearchResult;

public interface SearchListener {
    void onSearchCompleted(Query query, SearchResult searchResult);
    void onSearchFailed(Query query, Exception e);
}
