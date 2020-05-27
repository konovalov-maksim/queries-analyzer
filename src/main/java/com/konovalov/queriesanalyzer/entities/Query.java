package com.konovalov.queriesanalyzer.entities;

import javax.persistence.*;

@Entity
@Table(name = "query")
public class Query {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "text")
    private String text;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "google_search_result_id", referencedColumnName = "id")
    private SearchResult googleSearchResult;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "yandex_search_result_id", referencedColumnName = "id")
    private SearchResult yandexSearchResult;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "search_task_id", referencedColumnName = "id")
    private SearchTask searchTask;

    public Query() {}

    public Query(String text) {
        this.text = text;
    }

    public boolean isProcessed() {
        if (searchTask == null) throw new IllegalStateException("Search task is null");
        if (searchTask.getDoGoogleSearch()) {
            if (googleSearchResult == null) return false;
        }
        if (searchTask.getDoYandexSearch()) {
            if (yandexSearchResult == null) return false;
        }
        return true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public SearchResult getGoogleSearchResult() {
        return googleSearchResult;
    }

    public void setGoogleSearchResult(SearchResult googleSearchResult) {
        this.googleSearchResult = googleSearchResult;
    }

    public SearchResult getYandexSearchResult() {
        return yandexSearchResult;
    }

    public void setYandexSearchResult(SearchResult yandexSearchResult) {
        this.yandexSearchResult = yandexSearchResult;
    }

    public SearchTask getSearchTask() {
        return searchTask;
    }

    public void setSearchTask(SearchTask searchTask) {
        this.searchTask = searchTask;
    }
}
