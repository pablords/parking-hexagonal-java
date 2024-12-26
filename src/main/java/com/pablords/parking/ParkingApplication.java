package com.pablords.parking;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.pablords.parking.adapters.outbound.database.jpa.repositories.JpaRepositoryCar;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = JpaRepositoryCar.class)
@Slf4j
public class ParkingApplication implements CommandLineRunner {

	@Value("${app.name}")
	String appName;

	@Override
	public void run(String... args) {
		log.info("App is running {}", appName);
	}

	public static void main(String[] args) {
		SpringApplication.run(ParkingApplication.class, args);
	}

}
