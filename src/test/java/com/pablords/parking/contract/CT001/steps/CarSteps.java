package com.pablords.parking.contract.CT001.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pablords.parking.adapters.inbound.http.dtos.CarResponseDTO;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


public class CarSteps {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private HttpStatus responseStatus;
    private String responseContent;
    private final String PARKING_API_URL_CARS = "/cars";

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Given("That I am in the api endpoint {string}")
    public void that_i_am_in_the_api_endpoint(String endpoint) {
        assertEquals(endpoint, PARKING_API_URL_CARS);
    }

    @When("I create a car with the following details: {string}")
    public void i_create_a_car_with_the_following_details(String jsonPath) throws Exception {
        var jsonFileContent = new String(Files.readAllBytes(Paths.get(jsonPath)));

        try {
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
        var objectMapper = new ObjectMapper();
        try {
            CarResponseDTO carResponseDTO = objectMapper.readValue(responseContent, CarResponseDTO.class);
            assertNotNull(carResponseDTO.getId());
            assertEquals(responseStatus.value(), status);
        } catch (JsonProcessingException e) {
            assertEquals(responseStatus.value(), status);
        }
    }

}
