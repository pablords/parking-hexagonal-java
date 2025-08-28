package com.pablords.parking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import lombok.extern.slf4j.Slf4j;

import java.time.Clock;

@Configuration
@Profile("!component-test & !integration-test & !unit-test")
@Slf4j
public class ClockConfig {
  @Bean
  public Clock clock() {
    log.info(">>> Usando Clock de produção!");
    return Clock.systemDefaultZone();
  }
}
