package br.com.beauty_book.search_filtering;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class SearchFilteringApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchFilteringApplication.class, args);
	}

}
