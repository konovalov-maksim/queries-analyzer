package com.konovalov.queriesanalyzer.services.search.searchers;

import com.konovalov.queriesanalyzer.entities.*;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service(value = "yandexSearcher")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class YandexSearcher extends Searcher {

    private int regionId = 213;

    public YandexSearcher(Query query) {
        super(query);
    }

    @Override
    HttpUrl generateSearchUrl() {
        return HttpUrl.parse("https://www.yandex.ru/search/").newBuilder()
                .addQueryParameter("text", this.getQuery().getText())
                .addQueryParameter("lr", String.valueOf(regionId))
                .build();
    }

    @Override
    Headers generateHeaders() {
        return new Headers.Builder()
                .add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:78.0) Gecko/20100101 Firefox/78.0")
                .add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .add("Host", "www.yandex.ru")
                .add("Accept-Language:", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
                .add("Connection", "keep-alive")
                .add("Referer", "https://www.yandex.ru")
                .add("Upgrade-Insecure-Requests", "1")
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

        int adsCount = extractAdsCount(doc);
        searchResult.setAdsCount(adsCount);

        searchResult.setSearchEngine(SearchEngine.YANDEX);
        return searchResult;
    }

    private List<Page> extractOrganicPages(Document doc) {
        Elements liList = doc.select("li.serp-item");
        List<Page> organicPages = liList.stream()
                .filter(li -> li.attributes().size() == 4)
                .map(li -> li.select("h2 > a.organic__url").first())
                .filter(Objects::nonNull)
                .map(a -> new Page(a.attr("href")))
                .peek(System.out::println)
                .collect(Collectors.toList());
        for (int i = 0; i < organicPages.size(); i++) organicPages.get(i).setPosition(i);
        return organicPages;
    }

    private long extractTotalResults(Document doc) {
        Element totalResultsEl = doc.selectFirst("div.serp-adv__found");
        if (totalResultsEl == null || totalResultsEl.text().isEmpty()) return 0;
        //Напр. 'Нашлось 26 млн результатов'
        String totalResultsStr = totalResultsEl.text();
        Matcher numMatcher = Pattern.compile("\\d+").matcher(totalResultsStr);
        if (!numMatcher.find()) {
            //TODO залогировать тут ошибку парсинга
            return 0;
        }
        int totalResults = Integer.parseInt(numMatcher.group());
        int multiplier = 1;
        if (totalResultsStr.contains("тыс.")) multiplier = 1000;
        if (totalResultsStr.contains("млн")) multiplier = 1000000;
        return totalResults * multiplier;
    }

    private int extractAdsCount(Document doc) {
        Elements liList = doc.select("li.serp-item div.label_theme_direct");
        return liList.size();
    }

}
