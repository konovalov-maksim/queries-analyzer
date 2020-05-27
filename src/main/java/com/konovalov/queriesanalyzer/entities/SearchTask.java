package com.konovalov.queriesanalyzer.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "search_task")
public class SearchTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date_added", insertable = false, updatable = false)
    private Date dateAdded;

    @Column(name = "do_yandex_search")
    private Boolean doYandexSearch = false;

    @Column(name = "do_google_search")
    private Boolean doGoogleSearch = false;

    @Column(name = "status_id")
    private int statusId = 0;

    @OneToMany(mappedBy = "searchTask", cascade = CascadeType.ALL)
    private List<Query> queries;

    @Transient
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
