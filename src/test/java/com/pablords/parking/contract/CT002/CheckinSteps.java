package com.pablords.parking.contract.CT002;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pablords.parking.ParkingApplication;
import com.pablords.parking.adapters.inbound.http.dtos.CheckinRequestDTO;
import com.pablords.parking.adapters.inbound.http.dtos.CheckinResponseDTO;
import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.entities.Checkin;
import com.pablords.parking.core.ports.outbound.repositories.CheckinRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.SlotRepositoryPort;
import com.pablords.parking.core.valueobjects.Plate;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = ParkingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CheckinSteps {
    private MockMvc mockMvc;
    private HttpStatus responseStatus;
    private String responseContent;
    private final String PARKING_API_URL_CHECKINS = "/checkins";
    @MockBean
    private CheckinRepositoryPort checkinRepositoryPortMock;
    @MockBean
    private SlotRepositoryPort slotRepositoryPortMock;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        System.out.println("CheckinRepository Mock: " + checkinRepositoryPortMock);
        assertNotNull(checkinRepositoryPortMock, "Mock do repositório não foi injetado corretamente!");
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
        try {
            var jsonFileContent = new String(Files.readAllBytes(Paths.get(jsonPath)));
            var checkinToCreate = objectMapper.readValue(jsonFileContent, CheckinRequestDTO.class);
            var createdCheckin = new Checkin();
            var car = new Car(new Plate(checkinToCreate.getPlate()), checkinToCreate.getBrand(),
                    checkinToCreate.getColor(),
                    checkinToCreate.getModel());
            createdCheckin.setCar(car);
            createdCheckin.setId(UUID.fromString("f5d4b3b4-1b4b-4b4b-8b4b-4b4b4b4b4b4b"));


            when(checkinRepositoryPortMock.save(any(Checkin.class))).thenReturn(createdCheckin);
            verify(checkinRepositoryPortMock).save(any(Checkin.class));
            
            when(checkinRepositoryPortMock.findByPlate(any())).thenReturn(Optional.of(createdCheckin));
            when(checkinRepositoryPortMock.findById(any())).thenReturn(Optional.of(createdCheckin));


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

    @And("the response status should be {int}")
    public void the_response_status_should_be(int status) throws Exception {
        try {
            CheckinResponseDTO checkinResponseDTO = objectMapper.readValue(responseContent, CheckinResponseDTO.class);
            assertNotNull(checkinResponseDTO.getId(), "Checkin ID não foi retornado na resposta.");
            assertEquals(responseStatus.value(), status);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
    }

    @Then("the response should contain a check-in timestamp")
    public void the_response_should_contain_a_check_in_timestamp() throws Exception {
        try {
            CheckinResponseDTO checkinResponseDTO = objectMapper.readValue(responseContent, CheckinResponseDTO.class);
            assertNotNull(checkinResponseDTO.getCheckInTime(), "Checkin timestamp não foi retornado na resposta.");
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
    }

}
