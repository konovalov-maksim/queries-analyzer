package com.konovalov.queriesanalyzer.services.search.searchers;

import com.konovalov.queriesanalyzer.entities.Page;
import com.konovalov.queriesanalyzer.entities.Query;
import com.konovalov.queriesanalyzer.entities.SearchEngine;
import com.konovalov.queriesanalyzer.entities.SearchResult;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service(value = "googleSearcher")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class GoogleSearcher extends Searcher {

    @Autowired
    public GoogleSearcher(Query query) {
        super(query);
    }

    @Override
    HttpUrl generateSearchUrl() {
        return HttpUrl.parse("https://www.google.com/search").newBuilder()
                .addQueryParameter("q", getQuery().getText())
                .addQueryParameter("client", "firefox-b-d")
                .build();
    }

    @Override
    Headers generateHeaders() {
        return new Headers.Builder()
                .add("Host", "www.google.com")
                .add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:78.0) Gecko/20100101 Firefox/78.0")
                .add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .add("Accept-Language", "en-US,en;q=0.5")
                .add("Connection", "keep-alive")
                .add("Upgrade-Insecure-Requests", "1")
                .add("TE", "Trailers")
                .build();
    }

    @Override
    SearchResult parseSerp(String serpBody) {
        SearchResult searchResult = new SearchResult();
        Document doc = Jsoup.parse(serpBody);

        List<Page> organicPages = extractOrganicPages(doc);
        searchResult.addPages(organicPages);

        long pagesFound = extractTotalResults(doc);
        searchResult.setPagesFound(pagesFound);

        int adsCount = doc.select("li.ads-ad").size();
        searchResult.setAdsCount(adsCount);

        searchResult.setSearchEngine(SearchEngine.GOOGLE);
        return searchResult;
    }

    private List<Page> extractOrganicPages(Document doc) {
        List<Page> organicPages = new ArrayList<>();
        Elements links = doc.select("div#res div.g div.r > a");
        for (int i = 0; i < links.size(); i++) {
            Page page = new Page(links.get(i).attr("href"));
            page.setPosition(i);
            organicPages.add(page);
        }
        return organicPages;
    }

    private long extractTotalResults(Document doc) {
        try {
            Element totalResultsEl = doc.getElementById("result-stats");
            if (totalResultsEl == null) return 0;
            String totalResultsStr = totalResultsEl.ownText().replaceAll("[^\\d]", "");
            long totalResults = Long.parseLong(totalResultsStr);
            return totalResults;
        } catch (Exception e) {
            return 0;
            //TODO залогировать ошибку парсинга
        }
    }

}
