package com.konovalov.queriesanalyzer.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "query")
public class Query {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "text")
    private String text;

    @Column(name = "yandex_status_id")
    private Integer yandexStatusId = 0;

    @Column(name = "google_status_id")
    private Integer googleStatusId = 0;

    @OneToOne
    @JoinColumn(name = "google_search_result_id", referencedColumnName = "id")
    private SearchResult googleSearchResult;

    @OneToOne
    @JoinColumn(name = "yandex_search_result_id", referencedColumnName = "id")
    private SearchResult yandexSearchResult;

    @ManyToOne
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

    public Integer getYandexStatusId() {
        return yandexStatusId;
    }

    public void setYandexStatusId(Integer yandexStatusId) {
        this.yandexStatusId = yandexStatusId;
    }

    public Integer getGoogleStatusId() {
        return googleStatusId;
    }

    public void setGoogleStatusId(Integer googleStatusId) {
        this.googleStatusId = googleStatusId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Query query = (Query) o;
        return Objects.equals(text, query.text) &&
                Objects.equals(yandexStatusId, query.yandexStatusId) &&
                Objects.equals(googleStatusId, query.googleStatusId) &&
                Objects.equals(googleSearchResult, query.googleSearchResult) &&
                Objects.equals(yandexSearchResult, query.yandexSearchResult) &&
                Objects.equals(searchTask, query.searchTask);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, yandexStatusId, googleStatusId, googleSearchResult, yandexSearchResult, searchTask);
    }

    @Override
    public String toString() {
        return text;
    }
}
