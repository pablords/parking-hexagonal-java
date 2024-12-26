package com.pablords.parking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.pablords.parking.core.ports.inbound.services.CarServicePort;
import com.pablords.parking.core.ports.outbound.repositories.CarRepositoryPort;
import com.pablords.parking.core.services.CarServiceImpl;

@Configuration
public class BeanConfiguration {

    @Bean
    CarServicePort carService(CarRepositoryPort carRepositoryPort) {
        return new CarServiceImpl(carRepositoryPort);
    }


}
