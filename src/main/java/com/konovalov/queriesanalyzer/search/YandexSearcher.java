package com.konovalov.queriesanalyzer.search;

import com.konovalov.queriesanalyzer.entities.Query;
import com.konovalov.queriesanalyzer.entities.SearchParams;
import com.konovalov.queriesanalyzer.entities.SearchResult;
import okhttp3.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class YandexSearcher {

    private OkHttpClient client;
    private Headers headers;
    private SearchParams params;
    private Integer regionId;

//    private Deque<Query> queries = new ConcurrentLinkedDeque<>();


    public YandexSearcher() {
        initialize();
    }

    public void search(Query query) {
        Request request = new Request.Builder()
                .url(getUrl(query.getText()))
                .headers(headers)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String responseBody = response.peekBody(Long.MAX_VALUE).string();

            YandexResultProcessor resultProcessor = new YandexResultProcessor();
            SearchResult searchResult = resultProcessor.getSearchResult(responseBody);

            Files.write(Paths.get(".").resolve("html/" + query.getText() + ".htm"), responseBody.getBytes(),
                    StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HttpUrl getUrl(String query) {
        return HttpUrl.parse("https://www.yandex.ru/search/").newBuilder()
                .addQueryParameter("text", query)
                .addQueryParameter("lr", regionId.toString())
                .build();
    }

    private void initialize() {
        client = new OkHttpClient.Builder().build();
        headers = new Headers.Builder()
                .add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:75.0) Gecko/20100101 Firefox/75.0")
                .add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .add("Host", "www.yandex.ru")
                .add("Accept-Language:", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
                .add("Content-type", "application/json")
                .add("Connection", "keep-alive")
                .add("Referer", "https://www.yandex.ru")
                .build();
        this.params = new SearchParams();
        this.regionId = 213;
    }

}
