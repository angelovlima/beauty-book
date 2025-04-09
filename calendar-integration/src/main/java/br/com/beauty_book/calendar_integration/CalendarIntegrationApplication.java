package br.com.beauty_book.calendar_integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class CalendarIntegrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalendarIntegrationApplication.class, args);
	}

}
