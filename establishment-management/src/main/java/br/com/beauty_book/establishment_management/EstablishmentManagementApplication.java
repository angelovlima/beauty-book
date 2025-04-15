package br.com.beauty_book.establishment_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class EstablishmentManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(EstablishmentManagementApplication.class, args);
	}

}
