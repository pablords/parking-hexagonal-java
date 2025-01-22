package com.pablords.parking.component;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import com.pablords.parking.adapters.outbound.database.jpa.repositories.JpaCarRepository;
import com.pablords.parking.adapters.outbound.database.jpa.repositories.JpaCheckinRepository;
import com.pablords.parking.adapters.outbound.database.jpa.repositories.JpaCheckoutRepository;
import com.pablords.parking.adapters.outbound.database.jpa.repositories.JpaSlotRepository;

import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@CucumberContextConfiguration
@ActiveProfiles("component-test")
public class CucumberSpringConfiguration {
    @MockBean
    private JpaCarRepository jpaCarRepositoryMock;
    @MockBean
    private JpaSlotRepository jpaSlotRepositoryMock;
    @MockBean
    private JpaCheckinRepository jpaCheckinRepositoryMock;
    @MockBean
    private JpaCheckoutRepository jpaCheckoutRepositoryMock;

    @Autowired
    private Environment environment;

    @Before
    public void setUp() {
        System.out.println("🔎 Active Profiles: " + String.join(", ", environment.getActiveProfiles()));
    }
}