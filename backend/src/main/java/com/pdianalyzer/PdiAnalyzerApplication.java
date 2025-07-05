package com.pdianalyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@ComponentScan(basePackages = { "com.pdianalyzer", "com.smarthirepro" })
@EntityScan(basePackages = { "com.pdianalyzer.domain.model", "com.smarthirepro.domain.model" })
@EnableJpaRepositories(basePackages = { "com.pdianalyzer.domain.repository", "com.smarthirepro.domain.repositories" })
public class PdiAnalyzerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PdiAnalyzerApplication.class, args);
	}

}
