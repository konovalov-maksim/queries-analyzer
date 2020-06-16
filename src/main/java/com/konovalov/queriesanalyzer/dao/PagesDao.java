package com.konovalov.queriesanalyzer.dao;

import com.konovalov.queriesanalyzer.entities.Page;
import org.springframework.data.repository.CrudRepository;

public interface PagesDao extends CrudRepository<Page, Long> {
}
