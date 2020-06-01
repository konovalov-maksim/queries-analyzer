package com.konovalov.queriesanalyzer.models;

public class SearchTaskModel {

    private String queries;

    private String name;

    private boolean doYandexSearch;

    private boolean doGoogleSearch;

    public String getQueries() {
        return queries;
    }

    public void setQueries(String queries) {
        this.queries = queries;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
