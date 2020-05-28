package com.konovalov.queriesanalyzer.models;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SearchTaskModel {

    private String queries;

    private boolean doYandexSearch;

    private boolean doGoogleSearch;

    public List<String> getQueriesList() {
        if (queries == null || queries.isEmpty()) return Collections.emptyList();
        return Arrays.stream(queries.split("\r\n"))
                .distinct()
                .collect(Collectors.toList());
    }

    public String getQueries() {
        return queries;
    }

    public void setQueries(String queries) {
        this.queries = queries;
    }

    public boolean getDoYandexSearch() {
        return doYandexSearch;
    }

    public void setDoYandexSearch(boolean doYandexSearch) {
        this.doYandexSearch = doYandexSearch;
    }

    public boolean getDoGoogleSearch() {
        return doGoogleSearch;
    }

    public void setDoGoogleSearch(boolean doGoogleSearch) {
        this.doGoogleSearch = doGoogleSearch;
    }
}
