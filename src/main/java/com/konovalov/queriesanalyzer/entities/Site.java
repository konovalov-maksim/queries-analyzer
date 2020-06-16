package com.konovalov.queriesanalyzer.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "site")
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "domain")
    private String domain;

    @Column(name = "google_index_size")
    private Integer googleIndexSize;

    @Column(name = "yandex_index_size")
    private Integer yandexIndexSize;

    @Column(name = "iks")
    private Integer iks;

    public Site() {
    }

    public Site(String domain) {
        this.domain = domain;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Integer getGoogleIndexSize() {
        return googleIndexSize;
    }

    public void setGoogleIndexSize(Integer googleIndexSize) {
        this.googleIndexSize = googleIndexSize;
    }

    public Integer getYandexIndexSize() {
        return yandexIndexSize;
    }

    public void setYandexIndexSize(Integer yandexIndexSize) {
        this.yandexIndexSize = yandexIndexSize;
    }

    public Integer getIks() {
        return iks;
    }

    public void setIks(Integer iks) {
        this.iks = iks;
    }

    @Override
    public String toString() {
        return domain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Site site = (Site) o;
        return Objects.equals(domain, site.domain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain);
    }
}
