package com.konovalov.queriesanalyzer.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchResult that = (SearchResult) o;
        return Objects.equals(pagesFound, that.pagesFound) &&
                Objects.equals(adsCount, that.adsCount) &&
                Objects.equals(dateAdded, that.dateAdded) &&
                searchEngine == that.searchEngine &&
                Objects.equals(pages, that.pages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pagesFound, adsCount, dateAdded, searchEngine, pages);
    }
}
