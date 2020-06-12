package com.konovalov.queriesanalyzer.dao;

import com.konovalov.queriesanalyzer.entities.StoredCookie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CookiesDao extends CrudRepository<StoredCookie, Long> {
    List<StoredCookie> findByDomain(String domain);
}

