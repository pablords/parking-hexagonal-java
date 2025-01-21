package com.pablords.parking.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import com.pablords.parking.core.ports.outbound.repositories.CarRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.CheckinRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.CheckoutRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.SlotRepositoryPort;

import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@CucumberContextConfiguration
@ActiveProfiles("component-test")
public class CucumberSpringConfiguration {
    @MockBean
    private CarRepositoryPort carRepositoryPortMock;
    @MockBean
    private SlotRepositoryPort slotRepositoryPortMock;
    @MockBean
    private CheckinRepositoryPort checkinRepositoryPortMock;
    @MockBean
    private CheckoutRepositoryPort checkoutRepositoryPortMock;

    @Autowired
    private Environment environment;

    @Before
    public void setUp() {
        System.out.println("🔎 Active Profiles: " + String.join(", ", environment.getActiveProfiles()));
    }
}