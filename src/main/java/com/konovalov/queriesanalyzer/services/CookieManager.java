package com.konovalov.queriesanalyzer.services;

import com.konovalov.queriesanalyzer.dao.CookiesDao;
import com.konovalov.queriesanalyzer.entities.StoredCookie;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class CookieManager implements CookieJar {

    private final CookiesDao cookiesDao;

    @Autowired
    public CookieManager(CookiesDao cookiesDao) {
        this.cookiesDao = cookiesDao;
    }

    @NotNull
    @Override
    public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
        return new ArrayList<>();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> cookies) {
        final String host = httpUrl.host();
        List<StoredCookie> storedCookies = cookies.stream()
                .map(c -> convertToStoredCookie(host, c))
                .collect(Collectors.toList());
        cookiesDao.saveAll(storedCookies);
    }

    private StoredCookie convertToStoredCookie(String domain, Cookie cookie) {
        StoredCookie storedCookie = new StoredCookie();
        storedCookie.setDomain(domain);
        storedCookie.setName(cookie.name());
        storedCookie.setValue(cookie.value());
        storedCookie.setExpiresAt(cookie.expiresAt());
        storedCookie.setPath(cookie.path());
        storedCookie.setHostOnly(cookie.hostOnly());
        storedCookie.setHttpOnly(cookie.httpOnly());
        storedCookie.setSecure(cookie.secure());
        return storedCookie;
    }

    private Cookie convertFromStoredCookie(StoredCookie storedCookie) {
        Cookie.Builder builder = new Cookie.Builder();
        if (storedCookie.getDomain() != null) {
            if (storedCookie.getHostOnly())
                builder.hostOnlyDomain(storedCookie.getDomain());
            else
                builder.domain(storedCookie.getDomain());
        }
        if (storedCookie.getName() != null)
            builder.name(storedCookie.getName());
        if (storedCookie.getValue() != null)
            builder.value(storedCookie.getValue());
        if (storedCookie.getExpiresAt() != null)
            builder.expiresAt(storedCookie.getExpiresAt());
        if (storedCookie.getPath() != null)
            builder.path(storedCookie.getPath());
        if (storedCookie.getHttpOnly() != null && storedCookie.getHttpOnly())
            builder.httpOnly();
        if (storedCookie.getSecure() != null && storedCookie.getSecure())
            builder.secure();
        return builder.build();
    }
}
