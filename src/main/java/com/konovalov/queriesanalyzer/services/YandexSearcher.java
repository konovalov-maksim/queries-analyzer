package com.konovalov.queriesanalyzer.services;

import com.konovalov.queriesanalyzer.entities.Page;
import com.konovalov.queriesanalyzer.entities.Query;
import com.konovalov.queriesanalyzer.entities.SearchEngine;
import com.konovalov.queriesanalyzer.entities.SearchResult;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service(value = "yandexSearcher")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class YandexSearcher implements Runnable{

    private OkHttpClient client;
    private final Query query;
    private int regionId = 213;
    private final Headers headers = new Headers.Builder()
            .add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:78.0) Gecko/20100101 Firefox/78.0")
            .add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
            .add("Host", "www.yandex.ru")
            .add("Accept-Language:", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
//            .add("Content-type", "application/json")
            .add("Connection", "keep-alive")
            .add("Cookie", "ys=wprid.1591950416046518-1695894980627433340800251-production-app-host-vla-web-yp-144#mailchrome.8-23-2; i=CZnXErsstZEGrCBMhhbaGnvf/NGsxnC1SB/JhxQ/xry38PCFBFhCXOCrgMFL+BOjfvfXSIOfpGrTKw6HFdNXNr74GbE=; yandexuid=4597242861591948053; yp=1623486420.p_sw.1591950420#1592552858.szm.1_5:1280x720:1279x207; spravka=dD0xNTkxOTQ4NzQ1O2k9OTMuMTU3LjE3NS4zOTt1PTE1OTE5NDg3NDU0OTgxOTUyMjU7aD05MzA0MTEwM2IzMjA2MmJiMjE3ODhlYzNmMzY5MDYzYw==; ar=1591948736929629-754772")
            .add("Referer", "https://www.yandex.ru")
            .add("Upgrade-Insecure-Requests", "1")
            .build();
    private final HttpUrl searchUrl;

    public YandexSearcher(Query query) {
        this.query = query;
        searchUrl = HttpUrl.parse("https://www.yandex.ru/search/").newBuilder()
                .addQueryParameter("text", query.getText())
                .addQueryParameter("lr", String.valueOf(regionId))
                .build();
    }

    @Override
    public void run() {
        try {
            String serpBody = loadSerp();
            saveDebugPage(serpBody);
            parseSerp(serpBody);
        } catch (IOException e) {
            e.printStackTrace();
            //TODO реализовать обработку
        }
    }

    private String loadSerp() throws IOException {
        Request request = new Request.Builder()
                .url(searchUrl)
                .headers(headers)
                .build();
        Response response = client.newCall(request).execute();
        String serpBody = response.peekBody(Long.MAX_VALUE).string();
        return serpBody;
    }

    private SearchResult parseSerp(String serpBody) {
        SearchResult searchResult = new SearchResult();
        Document doc = Jsoup.parse(serpBody);

        Elements liList = doc.select("li.serp-item");
        List<Page> organicPages = liList.stream()
                .filter(li -> li.attributes().size() == 4)
                .map(li -> li.select("h2 > a.organic__url").first())
                .filter(Objects::nonNull)
                .map(a -> new Page(a.attr("href")))
                .peek(System.out::println)
                .collect(Collectors.toList());
        for (int i = 0; i < organicPages.size(); i++) organicPages.get(i).setPosition(i);
        searchResult.addPages(organicPages);

        searchResult.setSearchEngine(SearchEngine.YANDEX);
        return searchResult;
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

}
