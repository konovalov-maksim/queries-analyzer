package com.konovalov.queriesanalyzer.entities;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "search_task")
public class SearchTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "date_added", insertable = false, updatable = false)
    private Date dateAdded;

    @Column(name = "do_yandex_search")
    private Boolean doYandexSearch = false;

    @Column(name = "do_google_search")
    private Boolean doGoogleSearch = false;

    @Column(name = "status_id")
    private int statusId = 0;

    @OneToMany(mappedBy = "searchTask", cascade = CascadeType.ALL)
    private List<Query> queries = new ArrayList<>();

    public void addQueries(List<Query> queries) {
        queries.forEach(q -> q.setSearchTask(this));
        this.queries.addAll(queries);
    }

    public int getProcessedQueriesCount() {
        return (int) queries.stream().filter(Query::isProcessed).count();
    }

    public int getProgress() {
        if (queries.isEmpty()) return 100;
        return getProcessedQueriesCount() * 100 / queries.size();
    }

    public String getDateAddedStr() {
        if (dateAdded == null) return "";
        return new SimpleDateFormat("dd.MM.yy hh:mm").format(dateAdded);
    }

    public String getDoGoogleSearchStr() {
        return doGoogleSearch ? "+" : "-";
    }

    public String getDoYandexSearchStr() {
        return doYandexSearch ? "+" : "-";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Boolean getDoYandexSearch() {
        return doYandexSearch;
    }

    public void setDoYandexSearch(Boolean doYandexSearch) {
        this.doYandexSearch = doYandexSearch;
    }

    public Boolean getDoGoogleSearch() {
        return doGoogleSearch;
    }

    public void setDoGoogleSearch(Boolean doGoogleSearch) {
        this.doGoogleSearch = doGoogleSearch;
    }

    public List<Query> getQueries() {
        return queries;
    }

    public void setQueries(List<Query> queries) {
        this.queries = queries;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }
}
