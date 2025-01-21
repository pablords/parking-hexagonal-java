package com.pablords.parking.integration.CT002;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pablords.parking.adapters.inbound.http.dtos.CheckinResponseDTO;

import io.cucumber.java.Before;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class CheckinSteps {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private HttpStatus responseStatus;
    private String responseContent;
    private final String PARKING_API_URL_CHECKINS = "/checkins";
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

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

    @Dado("que o carro com placa {string} não está estacionado")
    public void thatIAmInTheApiEndpoint(String plate) {
        assertEquals("ABC1234", plate);
    }

    @Dado("que o carro com placa {string} está estacionado")
    public void the_car_with_plate_is_checked_in(String plate) {
        assertEquals("ABC1234", plate);
    }

    @Quando("o cliente envia uma solicitação de check-in com {string}")
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

    @Quando("o cliente envia uma solicitação de check-in inválida com {string}")
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

    // @Then("the slot with id {int} should be occupied")
    // public void theSlotWithIdShouldBeOccupied(int slotId) {
    // var isOccupied = jdbcTemplate.queryForObject("SELECT occupied FROM slots
    // WHERE id = ?", new Object[] { slotId },
    // Boolean.class);
    // assertEquals(true, isOccupied);
    // }

    @Entao("o status da resposta do checkin deve ser {int}")
    public void theResponseStatusShouldBe(int status) throws Exception {
        CheckinResponseDTO checkinResponseDTO = objectMapper.readValue(responseContent, CheckinResponseDTO.class);
        switch (responseStatus) {
            case CREATED:
                assertNotNull(checkinResponseDTO.getId());
                assertEquals(responseStatus.value(), status);
                break;
            case UNPROCESSABLE_ENTITY:
                assertEquals(responseStatus.value(), status);
                break;
            case BAD_REQUEST:
                assertEquals(responseStatus.value(), status);
                break;
            default:
                assertEquals(responseStatus.value(), HttpStatus.INTERNAL_SERVER_ERROR.value());
                break;
        }

    }

    @E("a resposta deve conter um timestamp de check-in")
    public void the_response_should_contain_a_check_in_timestamp() throws Exception {
        CheckinResponseDTO checkinResponseDTO = objectMapper.readValue(responseContent, CheckinResponseDTO.class);
        assertNotNull(checkinResponseDTO.getCheckInTime());
    }

}
