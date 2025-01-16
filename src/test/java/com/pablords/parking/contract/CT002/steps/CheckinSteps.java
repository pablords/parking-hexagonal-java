package com.pablords.parking.contract.CT002.steps;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pablords.parking.adapters.inbound.http.dtos.CheckinResponseDTO;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
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
        // Limpando a tabelas do banco H2
        jdbcTemplate.execute("DELETE FROM checkins");
        jdbcTemplate.execute("DELETE FROM slots");

        // Inserindo um slot disponível
        jdbcTemplate.execute("INSERT INTO slots (id, occupied) VALUES (1, false)");
        jdbcTemplate.execute("INSERT INTO slots (id,occupied) VALUES (2, false)");
        jdbcTemplate.execute("INSERT INTO slots (id,occupied) VALUES (3, false)");
        jdbcTemplate.execute("INSERT INTO slots (id,occupied) VALUES (4, false)");

        System.out.println("Slots criados no banco H2 para teste.");

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Given("the car with plate {string} is not checked in")
    public void the_car_with_plate_is_not_checked_in(String plate) {
        assertEquals("ABC1234", plate);
    }

    @Given("the car with plate {string} is checked in")
    public void the_car_with_plate_is_checked_in(String plate) {
        assertEquals("ABC1234", plate);
    }

    @When("the client sends a check-in request with {string}")
    public void a_car_with_payload(String jsonPath) throws Exception {
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

    @Then("the slot with id {int} should be occupied")
    public void the_slot_with_id_should_be_occupied(int slotId) {
        var isOccupied = jdbcTemplate.queryForObject("SELECT occupied FROM slots WHERE id = ?", new Object[] { slotId },
                Boolean.class);
        assertEquals(true, isOccupied);
    }

    @And("the response status should be {int}")
    public void the_response_status_should_be(int status) throws Exception {
        var objectMapper = new ObjectMapper();
        try {
            CheckinResponseDTO checkinResponseDTO = objectMapper.readValue(responseContent, CheckinResponseDTO.class);
            assertNotNull(checkinResponseDTO.getId());
            assertEquals(responseStatus.value(), status);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
    }

    @Then("the response should contain a check-in timestamp")
    public void the_response_should_contain_a_check_in_timestamp() throws Exception {
        var objectMapper = new ObjectMapper();
        try {
            CheckinResponseDTO checkinResponseDTO = objectMapper.readValue(responseContent, CheckinResponseDTO.class);
            assertNotNull(checkinResponseDTO.getCheckInTime());
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
    }
    

}
