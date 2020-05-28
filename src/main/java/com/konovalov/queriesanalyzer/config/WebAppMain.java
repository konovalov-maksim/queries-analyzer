package com.konovalov.queriesanalyzer.config;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
		"com.konovalov.queriesanalyzer.controllers",
		"com.konovalov.queriesanalyzer.models",
		"com.konovalov.queriesanalyzer.config"
})
public class WebAppMain {

	public static void main(String[] args) {
		SpringApplication.run(WebAppMain.class, args);
	}
}
