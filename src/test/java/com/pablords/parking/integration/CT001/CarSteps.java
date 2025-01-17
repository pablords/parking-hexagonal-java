package com.pablords.parking.integration.CT001;

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
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Given("That I am in the api endpoint {string}")
    public void thatIAmInTheApiEndpoint(String endpoint) {
        assertEquals(endpoint, PARKING_API_URL_CARS);
    }

    @When("I create a car with the following details: {string}")
    public void iCreateACarWithTheFollowingDetails(String jsonPath) throws Exception {
        try {
            var jsonFileContent = new String(Files.readAllBytes(Paths.get(jsonPath)));
            mockMvc.perform(post(PARKING_API_URL_CARS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonFileContent))
                    .andExpect(result -> {
                        responseStatus = HttpStatus.valueOf(result.getResponse().getStatus());
                        responseContent = result.getResponse().getContentAsString();
                    });
        } catch (Exception e) {
            System.out.println("ERROR: " +e.getMessage());
            responseStatus = HttpStatus.BAD_REQUEST;
            responseContent = e.getMessage();
        }
    }

    @Then("The response status should be {int}")
    public void theResponseStatusShouldBe(int status) throws Exception {
        try {
            CarResponseDTO carResponseDTO = objectMapper.readValue(responseContent, CarResponseDTO.class);
            if(responseStatus == HttpStatus.CREATED){
                assertNotNull(carResponseDTO.getId());
            }
            assertEquals(responseStatus.value(), status);
        } catch (JsonProcessingException e) {
    
        }
    }

}
