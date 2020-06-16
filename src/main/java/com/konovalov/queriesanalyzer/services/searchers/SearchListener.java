package com.konovalov.queriesanalyzer.services.searchers;

import com.konovalov.queriesanalyzer.entities.Query;
import com.konovalov.queriesanalyzer.entities.SearchResult;

public interface SearchListener {
    void onSearchCompleted(SearchResult searchResult);
    void onSearchFailed(Query query, Exception e);
}
