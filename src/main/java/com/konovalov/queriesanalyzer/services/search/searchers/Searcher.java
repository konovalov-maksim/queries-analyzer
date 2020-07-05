package com.konovalov.queriesanalyzer.services.search.searchers;

import com.konovalov.queriesanalyzer.entities.Query;
import com.konovalov.queriesanalyzer.entities.SearchResult;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public abstract class Searcher implements Runnable {

    private OkHttpClient client;
    private SearchListener searchListener;
    private final Query query;
    private final HttpUrl searchUrl;
    private final Headers headers;

    public Searcher(Query query) {
        this.query = query;
        searchUrl = generateSearchUrl();
        headers = generateHeaders();
    }

    @Override
    public void run() {
        try {
            String serpBody = loadSerp();
            saveDebugPage(serpBody);
            SearchResult searchResult = parseSerp(serpBody);
            if (searchListener != null) searchListener.onSearchCompleted(query, searchResult);
        } catch (Exception e) {
            e.printStackTrace();
            if (searchListener != null) searchListener.onSearchFailed(query, e);
            //TODO реализовать обработку
        }
    }

    abstract HttpUrl generateSearchUrl();

    abstract Headers generateHeaders();

    abstract SearchResult parseSerp(String serpBody);

    private String loadSerp() throws IOException {
        Request request = new Request.Builder()
                .url(searchUrl)
                .headers(headers)
                .build();
        Response response = client.newCall(request).execute();
        String serpBody = response.peekBody(Long.MAX_VALUE).string();
        return serpBody;
    }

    private void saveDebugPage(String responseBody) throws IOException {
        Files.write(Paths.get(".").resolve("html/" + query.getText() + ".htm"), responseBody.getBytes(),
                StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
    }

    public Query getQuery() {
        return query;
    }

    @Autowired
    public void setClient(OkHttpClient client) {
        this.client = client;
    }

    public void setSearchListener(SearchListener searchListener) {
        this.searchListener = searchListener;
    }

}
