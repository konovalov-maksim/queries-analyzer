package com.konovalov.queriesanalyzer.config;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {
		"com.konovalov.queriesanalyzer.controllers",
		"com.konovalov.queriesanalyzer.models",
		"com.konovalov.queriesanalyzer.config",
		"com.konovalov.queriesanalyzer.services",
		"com.konovalov.queriesanalyzer.entities",
})
@EnableJpaRepositories("com.konovalov.queriesanalyzer.dao")
@EnableScheduling
@EntityScan("com.konovalov.queriesanalyzer.entities")
public class WebAppMain {
	public static void main(String[] args) {
		SpringApplication.run(WebAppMain.class, args);
	}
}
