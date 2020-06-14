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

    //TODO реализовать удаление старых cookie

    @NotNull
    @Override
    public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
        String domain = httpUrl.topPrivateDomain();
        List<Cookie> cookies = cookiesDao.findByDomain(domain).stream()
                .map(this::convertFromStoredCookie)
                .filter(cookie -> cookie.expiresAt() > System.currentTimeMillis())
                .filter(cookie -> cookie.matches(httpUrl))
                .collect(Collectors.toList());
        return cookies;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            StoredCookie storedCookie = cookiesDao.findByDomainAndNameAndPath(cookie.domain(), cookie.name(), cookie.path());
            if (storedCookie != null) {
                //Если cookie с такой комбинацией domain + name + path было найдено ранее, обновляем его
                storedCookie.setValue(cookie.value());
                storedCookie.setExpiresAt(cookie.expiresAt());
                storedCookie.setHostOnly(cookie.hostOnly());
                storedCookie.setHttpOnly(cookie.httpOnly());
                storedCookie.setSecure(cookie.secure());
            } else {
                //Если такого cookie ранее не было сохранено, сохраняем
                storedCookie = convertToStoredCookie(cookie);
                cookiesDao.save(storedCookie);
            }
        }

    }

    private StoredCookie convertToStoredCookie(Cookie cookie) {
        StoredCookie storedCookie = new StoredCookie();
        storedCookie.setDomain(cookie.domain());
        storedCookie.setName(cookie.name());
        storedCookie.setPath(cookie.path());
        storedCookie.setValue(cookie.value());
        storedCookie.setExpiresAt(cookie.expiresAt());
        storedCookie.setHostOnly(cookie.hostOnly());
        storedCookie.setHttpOnly(cookie.httpOnly());
        storedCookie.setSecure(cookie.secure());
        return storedCookie;
    }

    private Cookie convertFromStoredCookie(StoredCookie storedCookie) {
        Cookie.Builder builder = new Cookie.Builder();
        builder.name(storedCookie.getName());
        if (storedCookie.getHostOnly())
            builder.hostOnlyDomain(storedCookie.getDomain());
        else
            builder.domain(storedCookie.getDomain());
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
