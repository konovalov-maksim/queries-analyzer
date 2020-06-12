package com.konovalov.queriesanalyzer.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "stored_cookie")
public class StoredCookie {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "domain")
    private String domain;

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private String value;

    @Column(name = "path")
    private String path;

    @Column(name = "expires_at")
    private Long expiresAt;

    @Column(name = "host_only")
    private Boolean hostOnly;

    @Column(name = "http_only")
    private Boolean httpOnly;

    @Column(name = "secure")
    private Boolean secure;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Boolean getHostOnly() {
        return hostOnly;
    }

    public void setHostOnly(Boolean hostOnly) {
        this.hostOnly = hostOnly;
    }

    public Boolean getHttpOnly() {
        return httpOnly;
    }

    public void setHttpOnly(Boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    public Boolean getSecure() {
        return secure;
    }

    public void setSecure(Boolean secure) {
        this.secure = secure;
    }

    @Override
    public String toString() {
        return (name != null ? name : "undefined") + "=" + (value != null ? value : "undefined");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoredCookie that = (StoredCookie) o;
        return Objects.equals(domain, that.domain) &&
                Objects.equals(name, that.name) &&
                Objects.equals(value, that.value) &&
                Objects.equals(path, that.path) &&
                Objects.equals(expiresAt, that.expiresAt) &&
                Objects.equals(hostOnly, that.hostOnly) &&
                Objects.equals(httpOnly, that.httpOnly) &&
                Objects.equals(secure, that.secure);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain, name, value, path, expiresAt, hostOnly, httpOnly, secure);
    }
}
