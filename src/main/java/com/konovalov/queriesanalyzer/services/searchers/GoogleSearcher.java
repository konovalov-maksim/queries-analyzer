package com.konovalov.queriesanalyzer.services.searchers;

import com.konovalov.queriesanalyzer.entities.Query;
import com.konovalov.queriesanalyzer.entities.SearchResult;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service(value = "googleSearcher")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class GoogleSearcher extends Searcher {

    @Autowired
    public GoogleSearcher(Query query) {
        super(query);
    }

    @Override
    HttpUrl generateSearchUrl() {
        return null;
    }

    @Override
    Headers generateHeaders() {
        return null;
    }

    @Override
    SearchResult parseSerp(String serpBody) {
        return null;
    }
}
