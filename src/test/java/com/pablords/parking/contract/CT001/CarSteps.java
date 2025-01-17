package com.pablords.parking.contract.CT001;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pablords.parking.ParkingApplication;
import com.pablords.parking.adapters.inbound.http.controllers.CarController;
import com.pablords.parking.adapters.inbound.http.dtos.CarRequestDTO;
import com.pablords.parking.adapters.inbound.http.dtos.CarResponseDTO;
import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.ports.outbound.repositories.CarRepositoryPort;
import com.pablords.parking.core.valueobjects.Plate;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = ParkingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CarSteps {
    @Autowired
    private CarController carController;
    @Autowired
    private MockMvc mockMvc;
    private HttpStatus responseStatus;
    private String responseContent;
    private final String PARKING_API_URL_CARS = "/cars";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private CarRepositoryPort carRepositoryPortMock;

    @BeforeEach
    public void setUp() {
        System.out.println("CarRepositoryPort Mock: " + carRepositoryPortMock);
        assertNotNull(carRepositoryPortMock, "Mock do repositório não foi injetado corretamente!");
    }

    @Given("That I am in the api endpoint {string}")
    public void that_i_am_in_the_api_endpoint(String endpoint) {
        assertEquals(endpoint, PARKING_API_URL_CARS);
    }

    @When("I create a car with the following details: {string}")
    public void i_create_a_car_with_the_following_details(String jsonPath) throws Exception {
        try {
            var jsonFileContent = new String(Files.readAllBytes(Paths.get(jsonPath)));
            CarRequestDTO carToCreate = objectMapper.readValue(jsonFileContent, CarRequestDTO.class);
            Car createdCar = new Car(new Plate(carToCreate.getPlate()), carToCreate.getBrand(), carToCreate.getColor(),
                    carToCreate.getModel());
            createdCar.setId(UUID.fromString("f5d4b3b4-1b4b-4b4b-8b4b-4b4b4b4b4b4b"));

            System.out.println("Mocking repository call...");
            when(carRepositoryPortMock.save(any(Car.class))).thenReturn(createdCar);
            verify(carRepositoryPortMock).save(any(Car.class));
            mockMvc.perform(post(PARKING_API_URL_CARS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonFileContent))
                    .andExpect(result -> {
                        responseStatus = HttpStatus.valueOf(result.getResponse().getStatus());
                        responseContent = result.getResponse().getContentAsString();
                    });
        } catch (Exception e) {
            responseStatus = HttpStatus.BAD_REQUEST;
            responseContent = e.getMessage();
        }
    }

    @Then("The response status should be {int}")
    public void the_response_status_should_be(int status) throws Exception {
        try {
            CarResponseDTO carResponseDTO = objectMapper.readValue(responseContent, CarResponseDTO.class);
            if (status == 201) {
                assertNotNull(carResponseDTO.getId());
            }
            assertEquals(status, responseStatus.value());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
