package com.konovalov.queriesanalyzer.entities;

import javax.persistence.*;

@IdClass(ResultToPageId.class)
@Entity
@Table(name = "result_to_page")
public class ResultToPage {

    @Id
    @Column(name = "result_id")
    private Long resultId;

    @Id
    @Column(name = "page_id")
    private Long pageId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "result_id", referencedColumnName = "id", insertable = false, updatable = false)
    private SearchResult searchResult;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "page_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Page page;

    @Column(name = "position")
    private Integer position;

    public Long getResultId() {
        return resultId;
    }

    public void setResultId(Long resultId) {
        this.resultId = resultId;
    }

    public Long getPageId() {
        return pageId;
    }

    public void setPageId(Long pageId) {
        this.pageId = pageId;
    }

    public SearchResult getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(SearchResult searchResult) {
        this.searchResult = searchResult;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

}
