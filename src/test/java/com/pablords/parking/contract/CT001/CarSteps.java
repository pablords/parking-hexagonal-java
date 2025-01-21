package com.pablords.parking.contract.CT001;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pablords.parking.adapters.inbound.http.dtos.CarResponseDTO;
import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.ports.outbound.repositories.CarRepositoryPort;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CarSteps {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CarRepositoryPort carRepositoryPortMock;
    
    private HttpStatus responseStatus;
    private String responseContent;
    private final String PARKING_API_URL_CARS = "/cars";
    private final ObjectMapper objectMapper = new ObjectMapper();



    @Before
    public void setUp() {
        when(carRepositoryPortMock.save(any(Car.class))).thenAnswer(invocation -> {
            Car car = invocation.getArgument(0);
            car.setId(UUID.fromString("f7f6b3e3-4b7b-4b7b-8b7b-4b7b7b7b7b7b"));
            return car;
        });
    }

    @Given("That I am in the api endpoint {string}")
    public void that_i_am_in_the_api_endpoint(String endpoint) {
        assertEquals(endpoint, PARKING_API_URL_CARS);
    }

    @When("I create a car with the following details: {string}")
    public void i_create_a_car_with_the_following_details(String jsonPath) throws Exception {
        var jsonFileContent = new String(Files.readAllBytes(Paths.get(jsonPath)));

        mockMvc.perform(post(PARKING_API_URL_CARS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonFileContent))
                .andExpect(result -> {
                    responseStatus = HttpStatus.valueOf(result.getResponse().getStatus());
                    responseContent = result.getResponse().getContentAsString();
                });
    }

    @Then("The response status should be {int}")
    public void the_response_status_should_be(int status) throws Exception {
        CarResponseDTO carResponseDTO = objectMapper.readValue(responseContent, CarResponseDTO.class);
        switch (responseStatus) {
            case CREATED:
                assertNotNull(carResponseDTO.getId());
                assertEquals(responseStatus.value(), status);
                break;
            case UNPROCESSABLE_ENTITY:
                assertEquals(responseStatus.value(), status);
                break;
            default:
                assertEquals(responseStatus.value(), HttpStatus.INTERNAL_SERVER_ERROR.value());
                break;
        }
    }

}
