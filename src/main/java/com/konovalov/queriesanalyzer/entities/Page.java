package com.konovalov.queriesanalyzer.entities;

import javax.persistence.*;

@Entity
@Table(name = "page")
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "url")
    private String url;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "site_id", referencedColumnName = "id")
    private Site site;

    @Column(name = "title")
    private String title;

    @Column(name = "h1")
    private String h1;

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

    @Override
    public String toString() {
        return url != null ? url : super.toString();
    }
}
