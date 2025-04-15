package br.com.beauty_book.professional_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class ProfessionalManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProfessionalManagementApplication.class, args);
	}

}
