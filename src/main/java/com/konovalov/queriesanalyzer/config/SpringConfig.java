package com.konovalov.queriesanalyzer.config;

import com.konovalov.queriesanalyzer.services.CookieManager;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.concurrent.TimeUnit;

@Configuration
public class SpringConfig {

    @Value("${httpClient.connectionTimeout}")
    private int connectionTimeout;
    @Value("${httpClient.readTimeout}")
    private int readTimeout;
    @Value("${httpClient.writeTimeout}")
    private int writeTimeout;

    private CookieManager cookieManager;

    @Autowired
    public void setCookieManager(CookieManager cookieManager) {
        this.cookieManager = cookieManager;
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_SINGLETON)
    public OkHttpClient getClient() {
        return new OkHttpClient.Builder()
                .cookieJar(cookieManager)
                .connectTimeout(connectionTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .build();
    }

}
