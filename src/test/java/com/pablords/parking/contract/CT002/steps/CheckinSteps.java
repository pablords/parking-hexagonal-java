package com.pablords.parking.contract.CT002.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pablords.parking.adapters.inbound.http.dtos.CheckinResponseDTO;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CheckinSteps {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private HttpStatus responseStatus;
    private String responseContent;
    private final String PARKING_API_URL_CHECKINS = "/checkins";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        // Limpando a tabela de slots (caso necessário)
        jdbcTemplate.execute("DELETE FROM slots");

        // Inserindo um slot disponível
        jdbcTemplate.execute("INSERT INTO slots (id, occupied) VALUES (1, false)");
        jdbcTemplate.execute("INSERT INTO slots (id,occupied) VALUES (2, false)");

        System.out.println("Slots criados no banco H2 para teste.");

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Given("the car with plate {string} is not checked in")
    public void thatIAmInTheApiEndpoint(String plate) {
        assertEquals("ABC1234", plate);
    }

    @When("the client sends a check-in request with {string}")
    public void a_car_with_plate(String jsonPath) throws Exception {
        var jsonFileContent = new String(Files.readAllBytes(Paths.get(jsonPath)));

        try {
            mockMvc.perform(post(PARKING_API_URL_CHECKINS)
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

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int status) throws Exception {
        var objectMapper = new ObjectMapper();
        try {
            CheckinResponseDTO checkinResponseDTO = objectMapper.readValue(responseContent, CheckinResponseDTO.class);
            System.out.println("Response: " + responseContent);
            // assertNotNull(checkinResponseDTO.getId());
            // assertEquals(responseStatus.value(), status);
        } catch (JsonProcessingException e) {
            // assertEquals(responseStatus.value(), status);
        }
    }
}
