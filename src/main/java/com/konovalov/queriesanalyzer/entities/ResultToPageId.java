package com.konovalov.queriesanalyzer.entities;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ResultToPageId implements Serializable {

    private Long resultId;

    private Long pageId;

    public ResultToPageId() {}

    public ResultToPageId(Long resultId, Long pageId) {
        this.resultId = resultId;
        this.pageId = pageId;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultToPageId that = (ResultToPageId) o;
        return Objects.equals(resultId, that.resultId) &&
                Objects.equals(pageId, that.pageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resultId, pageId);
    }
}
