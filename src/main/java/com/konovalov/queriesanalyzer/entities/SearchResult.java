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

    @Column(name = "ads_num")
    private Integer adsNum;

    @Column(name = "date_added")
    private Date dateAdded;

    @Column(name = "search_engine")
    @Enumerated(EnumType.STRING)
    private SearchEngine searchEngine;

    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ResultToPage> topPages = new ArrayList<ResultToPage>();

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

    public Integer getAdsNum() {
        return adsNum;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setAdsNum(Integer adsNum) {
        this.adsNum = adsNum;
    }

    public SearchEngine getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(SearchEngine searchEngine) {
        this.searchEngine = searchEngine;
    }

    public List<ResultToPage> getTopPages() {
        return topPages;
    }

    public void setTopPages(List<ResultToPage> topPages) {
        this.topPages = topPages;
    }
}
