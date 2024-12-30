package com.pablords.parking.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pablords.parking.core.entities.Slot;
import com.pablords.parking.core.ports.outbound.repositories.SlotRepositoryPort;

import java.util.stream.IntStream;

@Configuration
public class SlotConfig {

    @Bean
    public ApplicationRunner initializeSlots(SlotRepositoryPort slotRepository) {
        return args -> {
            IntStream.rangeClosed(1, 10).forEach(i -> {
                Slot slot = new Slot(); // Configure atributos, se necessário
                slotRepository.save(slot);
            });
            System.out.println("Slots iniciais criados!");
        };
    }
}

