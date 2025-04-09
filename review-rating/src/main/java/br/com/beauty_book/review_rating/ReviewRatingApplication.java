package br.com.beauty_book.review_rating;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ReviewRatingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReviewRatingApplication.class, args);
	}

}
