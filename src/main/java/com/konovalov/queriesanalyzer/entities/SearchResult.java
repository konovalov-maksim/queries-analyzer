package com.konovalov.queriesanalyzer.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "search_result")
public class SearchResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "pages_found")
    private Long pagesFound;

    @Column(name = "ads_count")
    private Integer adsCount;

    @Column(name = "date_added", insertable = false, updatable = false)
    private Date dateAdded;

    @Column(name = "search_engine")
    @Enumerated(EnumType.STRING)
    private SearchEngine searchEngine;

    @OneToMany(mappedBy = "searchResult", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("position")
    private List<Page> pages = new ArrayList<>();

    public void addPages(List<Page> newPages) {
        for (Page page : newPages) {
            page.setSearchResult(this);
            pages.add(page);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPagesFound() {
        return pagesFound;
    }

    public void setPagesFound(Long pagesFound) {
        this.pagesFound = pagesFound;
    }

    public Integer getAdsCount() {
        return adsCount;
    }


    public void setAdsCount(Integer adsCount) {
        this.adsCount = adsCount;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public SearchEngine getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(SearchEngine searchEngine) {
        this.searchEngine = searchEngine;
    }

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }
}
