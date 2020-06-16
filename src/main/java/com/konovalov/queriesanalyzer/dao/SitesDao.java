package com.konovalov.queriesanalyzer.dao;

import com.konovalov.queriesanalyzer.entities.Site;
import org.springframework.data.repository.CrudRepository;

public interface SitesDao extends CrudRepository<Site, Long> {
    Site findFirstByDomain(String domain);
}
