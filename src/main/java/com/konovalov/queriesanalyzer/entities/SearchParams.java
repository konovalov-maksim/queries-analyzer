package com.konovalov.queriesanalyzer.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "search_params")
public class SearchParams {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "threads_num")
    private Integer threadsNum = 5;

    @Column(name = "delay")
    private Integer delay = 500;

    public Integer getThreadsNum() {
        return threadsNum;
    }

    public void setThreadsNum(Integer threads) {
        this.threadsNum = threads;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }
}
