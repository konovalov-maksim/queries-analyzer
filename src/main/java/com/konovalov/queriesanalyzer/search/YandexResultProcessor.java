package com.konovalov.queriesanalyzer.search;

import com.konovalov.queriesanalyzer.entities.Page;
import com.konovalov.queriesanalyzer.entities.SearchResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class YandexResultProcessor {

    public SearchResult getSearchResult(String responseBody) {
        SearchResult searchResult = new SearchResult();
        Document doc = Jsoup.parse(responseBody);

        Elements liList = doc.select("li.serp-item");
        List<Page> organicPages = liList.stream()
                .filter(li -> li.attributes().size() == 4)
                .map(li -> li.select("h2 > a.organic__url").first())
                .filter(Objects::nonNull)
                .map(a -> new Page(a.attr("href")))
                .peek(System.out::println)
                .collect(Collectors.toList());
//        searchResult.setTopPages(organicPages);

        return searchResult;
    }
}
