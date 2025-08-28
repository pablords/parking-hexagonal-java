package com.pablords.parking.integration.CT002;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pablords.parking.adapters.inbound.http.dtos.CarRequestDTO;
import com.pablords.parking.adapters.inbound.http.dtos.CheckinResponseDTO;
import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.entities.Checkin;
import com.pablords.parking.core.ports.inbound.services.CheckinServicePort;
import com.pablords.parking.core.ports.outbound.repositories.CheckinRepositoryPort;
import com.pablords.parking.core.valueobjects.Plate;

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
    @Autowired
    private CheckinServicePort checkinServicePort;
    @Autowired
    private CheckinRepositoryPort checkinRepositoryPort;

    @Value("${features.request.path}")
    private String featuresRequestPath;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Before
    public void setUp() {
        // Limpando a tabelas do banco H2
        jdbcTemplate.execute("DELETE FROM checkins");
        jdbcTemplate.execute("DELETE FROM slots");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS checkins (id VARCHAR(255), plate VARCHAR(255), brand VARCHAR(255), color VARCHAR(255), model VARCHAR(255), checkin_time TIMESTAMP)");

        // Inserindo um slot disponível
        jdbcTemplate.execute("INSERT INTO slots (id, occupied) VALUES (1, false)");
        jdbcTemplate.execute("INSERT INTO slots (id,occupied) VALUES (2, false)");

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Dado("que o carro com placa {string} não está estacionado")
    public void thatIAmInTheApiEndpoint(String plate) {
        Optional<Checkin> existingCheckin = checkinRepositoryPort.findByPlate(plate);
        var slots = jdbcTemplate.query("SELECT occupied FROM slots", (rs, rowNum) -> rs.getBoolean("occupied"));
        slots.forEach(slot -> assertEquals(false, slot));;
        assertTrue(!existingCheckin.isPresent(), "O carro não deveria estar estacionado, mas está!");
    }

    @Dado("que o carro com placa {string} está estacionado")
    public void theCarWithPlateIsCheckedIn(String plate) throws Exception {
        var jsonPath = String.join("/", featuresRequestPath, "create-car-201.json");

        var jsonFileContent = new String(Files.readAllBytes(Paths.get(jsonPath)));
        var carToCreate = objectMapper.readValue(jsonFileContent, CarRequestDTO.class);
        var car = new Car(new Plate(carToCreate.plate()), carToCreate.brand(), carToCreate.color(),
                carToCreate.model());
        var checkin = checkinServicePort.checkIn(car);
        assertEquals(plate, checkin.getCar().getPlate().getValue(), "O carro deveria estar estacionado, mas não está!");
    }

    @Quando("o cliente envia uma solicitação de check-in com {string}")
    public void aCarWithPayload(String jsonPath) throws Exception {
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
    public void theClientSendsACheckInInvalidRequestWith(String jsonPath) throws Exception {

        var jsonFileContent = new String(Files.readAllBytes(Paths.get(jsonPath)));
        mockMvc.perform(post(PARKING_API_URL_CHECKINS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonFileContent))
                .andExpect(result -> {
                    responseStatus = HttpStatus.valueOf(result.getResponse().getStatus());
                    responseContent = result.getResponse().getContentAsString();
                });
    }

    @Entao("o slot com id {int} deve ser ocupado")
    public void theSlotWithIdShouldBeOccupied(int slotId) {
        var isOccupied = jdbcTemplate.queryForObject("SELECT occupied FROM slots WHERE id = ?", new Object[] { slotId },
                Boolean.class);
        assertEquals(true, isOccupied);
    }

    @Entao("o status da resposta do checkin deve ser {int}")
    public void theResponseStatusShouldBe(int status) throws Exception {
        CheckinResponseDTO checkinResponseDTO = objectMapper.readValue(responseContent, CheckinResponseDTO.class);
        switch (HttpStatus.valueOf(status)) {
            case CREATED:
                assertNotNull(checkinResponseDTO.id());
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
    public void theResponseShouldContainACheckInTimestamp() throws Exception {
        CheckinResponseDTO checkinResponseDTO = objectMapper.readValue(responseContent, CheckinResponseDTO.class);
        assertNotNull(checkinResponseDTO.checkInTime());
    }

}
