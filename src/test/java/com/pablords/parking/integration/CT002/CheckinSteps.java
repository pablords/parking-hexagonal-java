package com.pablords.parking.integration.CT002;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

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
import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.entities.Checkin;
import com.pablords.parking.core.entities.Checkout;
import com.pablords.parking.core.valueobjects.Plate;

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
    private final ObjectMapper objectMapper = new ObjectMapper();

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
    public void thatIAmInTheApiEndpoint(String plate) {
        assertEquals("ABC1234", plate);
    }

    @When("the client sends a check-in request with {string}")
    public void a_car_with_payload(String jsonPath) throws Exception {
        var jsonFileContent = new String(Files.readAllBytes(Paths.get(jsonPath)));
        mockMvc.perform(post(PARKING_API_URL_CHECKINS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonFileContent))
                .andExpect(result -> {
                    responseStatus = HttpStatus.valueOf(result.getResponse().getStatus());
                    responseContent = result.getResponse().getContentAsString();
                });
    }

    @Given("the car with plate {string} is checked in")
    public void the_car_with_plate_is_checked_in(String plate) {
        assertEquals("ABC1234", plate);
    }

    @When("the client sends a check-in invalid request with {string}")
    public void the_client_sends_a_check_in_invalid_request_with(String jsonPath) throws Exception {
        var jsonFileContent = new String(Files.readAllBytes(Paths.get(jsonPath)));
        mockMvc.perform(post(PARKING_API_URL_CHECKINS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonFileContent))
                .andExpect(result -> {
                    responseStatus = HttpStatus.valueOf(result.getResponse().getStatus());
                    responseContent = result.getResponse().getContentAsString();
                });
    }

    @Then("the slot with id {int} should be occupied")
    public void theSlotWithIdShouldBeOccupied(int slotId) {
        var isOccupied = jdbcTemplate.queryForObject("SELECT occupied FROM slots WHERE id = ?", new Object[] { slotId },
                Boolean.class);
        assertEquals(true, isOccupied);
    }

    @And("the response status should be {int}")
    public void theResponseStatusShouldBe(int status) throws Exception {
        try {
            CheckinResponseDTO checkinResponseDTO = objectMapper.readValue(responseContent, CheckinResponseDTO.class);
            switch (responseStatus) {
                case CREATED:
                    assertNotNull(checkinResponseDTO.getId());
                    assertEquals(responseStatus.value(), status);
                    break;
                case UNPROCESSABLE_ENTITY:
                    assertEquals(responseStatus.value(), status);
                    break;
                default:
                    assertEquals(responseStatus.value(), HttpStatus.INTERNAL_SERVER_ERROR.value());
                    break;
            }
        } catch (JsonProcessingException e) {
        }
    }

    @Then("the response should contain a check-in timestamp")
    public void the_response_should_contain_a_check_in_timestamp() throws Exception {
        try {
            CheckinResponseDTO checkinResponseDTO = objectMapper.readValue(responseContent, CheckinResponseDTO.class);
            assertNotNull(checkinResponseDTO.getCheckInTime());
        } catch (JsonProcessingException e) {
        }
    }

}
