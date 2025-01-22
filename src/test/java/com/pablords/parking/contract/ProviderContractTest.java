package com.pablords.parking.contract;

import au.com.dius.pact.provider.junit5.*;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;



import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.ports.outbound.repositories.CarRepositoryPort;
import com.pablords.parking.core.valueobjects.Plate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Provider("ParkingService")
@PactFolder("src/test/java/com/pablords/parking/resources/contracts") // Caminho para os contratos "mockados"
@ActiveProfiles("contract-test")
class ProviderContractTest {

    @LocalServerPort
    int port;

    @MockBean
    private CarRepositoryPort carRepositoryPort; // Mock do repositório


    @BeforeEach
    void setup(PactVerificationContext context) {
        // Configurando o mock para retornar um carro específico
        var car = new Car(new Plate("ABC1234"), "Toyota", "Red", "Sedan");
        car.setId(UUID.fromString("f5d4b3b4-1b4b-4b4b-8b4b-4b4b4b4b4b4b"));
        when(carRepositoryPort.findByPlate("ABC1234"))
                .thenReturn(Optional.of(car));
        System.out.println("Mock configurado para findByPlate(1234)");

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
