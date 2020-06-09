package com.konovalov.queriesanalyzer.services;

import com.konovalov.queriesanalyzer.entities.Query;

public interface Searcher {
    void doSearch(Query query);
}
