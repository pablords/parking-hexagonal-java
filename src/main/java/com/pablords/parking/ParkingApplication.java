package com.pablords.parking;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.pablords.parking.adapters.outbound.database.jpa.repositories.JpaRepositoryCar;
import com.pablords.parking.adapters.outbound.database.jpa.repositories.JpaRepositoryCheckin;
import com.pablords.parking.adapters.outbound.database.jpa.repositories.JpaRepositoryCheckout;
import com.pablords.parking.adapters.outbound.database.jpa.repositories.JpaRepositorySlot;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = {
		JpaRepositoryCar.class,
		JpaRepositorySlot.class,
		JpaRepositoryCheckin.class,
		JpaRepositoryCheckout.class
})
@Slf4j
public class ParkingApplication implements CommandLineRunner {

	@Value("${app.name}")
	String appName;

	@Override
	public void run(String... args) {
		log.info("{} app is running", appName);
	}

	public static void main(String[] args) {
		SpringApplication.run(ParkingApplication.class, args);
	}

}
