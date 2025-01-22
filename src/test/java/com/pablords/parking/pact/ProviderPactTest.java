package com.pablords.parking.pact;

import au.com.dius.pact.provider.junit5.*;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Provider("ParkingService")
@PactFolder("src/test/resources/pacts") // Caminho para os contratos "mockados"
public class ProviderPactTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setup(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", port));
    }

    @State("Existe um carro cadastrado com a placa ABC1234")
    public void setupCar() {
        System.out.println("Configurando o estado: Carro cadastrado no sistema");
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void validatePactContract(PactVerificationContext context) {
        context.verifyInteraction();
    }
}

