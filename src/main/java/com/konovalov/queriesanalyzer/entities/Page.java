package com.konovalov.queriesanalyzer.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "page")
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "title")
    private String title;

    @Column(name = "h1")
    private String h1;

    @Column(name = "position")
    private Integer position;

    @Column(name = "date_processed")
    private Date dateProcessed;

    @Column(name = "status_id")
    private Integer statusId = 0;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "site_id", referencedColumnName = "id")
    private Site site;

    @ManyToOne
    @JoinColumn(name = "search_result_id", referencedColumnName = "id")
    private SearchResult searchResult;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Status status;

    public Page() {
    }

    public Page(String url) {
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getH1() {
        return h1;
    }

    public void setH1(String h1) {
        this.h1 = h1;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Date getDateProcessed() {
        return dateProcessed;
    }

    public void setDateProcessed(Date dateProcessed) {
        this.dateProcessed = dateProcessed;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public SearchResult getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(SearchResult searchResult) {
        this.searchResult = searchResult;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return url != null ? url : super.toString();
    }
}
