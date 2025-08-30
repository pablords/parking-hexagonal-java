package com.pablords.parking.component.CT002;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pablords.parking.adapters.inbound.http.dtos.CheckinResponseDTO;
import com.pablords.parking.adapters.inbound.http.handlers.ApiErrorDTO;
import com.pablords.parking.adapters.outbound.database.jpa.models.CarModel;
import com.pablords.parking.adapters.outbound.database.jpa.models.CheckinModel;
import com.pablords.parking.adapters.outbound.database.jpa.models.CheckoutModel;
import com.pablords.parking.adapters.outbound.database.jpa.models.SlotModel;
import com.pablords.parking.adapters.outbound.database.jpa.repositories.springdata.JpaCarRepository;
import com.pablords.parking.adapters.outbound.database.jpa.repositories.springdata.JpaCheckinRepository;
import com.pablords.parking.adapters.outbound.database.jpa.repositories.springdata.JpaCheckoutRepository;
import com.pablords.parking.adapters.outbound.database.jpa.repositories.springdata.JpaSlotRepository;
import com.pablords.parking.component.TestUtils;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class CheckinSteps {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private JpaSlotRepository jpaSlotRepositoryMock;
  @Autowired
  private JpaCheckinRepository jpaCheckinRepositoryMock;
  @Autowired
  private JpaCheckoutRepository jpaCheckoutRepositoryMock;
  @Autowired
  private JpaCarRepository jpaCarRepositoryMock;

  private CheckinModel createdCheckin;
  private ArrayList<SlotModel> slots;
  private CheckoutModel checkout;
  private CarModel car;

  private HttpStatus responseStatus;
  private String responseContent;
  private final String PARKING_API_URL_CHECKINS = "/checkins";
  private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

  @Before
  public void setUp() {
    slots = TestUtils.createSlots();
    car = TestUtils.createCarModel();
    checkout = new CheckoutModel();
    createdCheckin = TestUtils.createCheckinModel(car, slots);
    TestUtils.mockSlotRepository(jpaSlotRepositoryMock, slots);
  }

  @After
  public void tearDown() {
    slots = null;
    car = null;
    checkout = null;
    createdCheckin = null;
    Mockito.reset();
  }

  @Dado("que o carro com placa {string} não está estacionado")
  public void theCarWithPlateIsNotCheckedIn(String plate) {
    when(jpaCheckinRepositoryMock.findLatestByCarPlate(any())).thenReturn(Optional.ofNullable(null));
    when(jpaCheckoutRepositoryMock.findByCheckinId(any())).thenReturn(Optional.ofNullable(null));
    when(jpaCarRepositoryMock.save(any())).thenReturn(car);
    when(jpaCheckinRepositoryMock.save(any(CheckinModel.class))).thenReturn(createdCheckin);
    Optional<CheckinModel> existingCheckin = jpaCheckinRepositoryMock.findLatestByCarPlate(plate);
    assertTrue(!existingCheckin.isPresent(), "O carro não deveria estar estacionado, mas está!");
  }

  @Dado("que o carro com placa {string} está estacionado")
  public void theCarWithPlateIsCheckedIn(String plate) {
    slots.get(0).setOccupied(true);
    when(jpaCheckinRepositoryMock.findLatestByCarPlate(any())).thenReturn(Optional.of(createdCheckin));
    when(jpaCheckoutRepositoryMock.findByCheckinId(createdCheckin.getId())).thenReturn(Optional.empty());
    Optional<CheckinModel> existingCheckin = jpaCheckinRepositoryMock.findLatestByCarPlate(plate);
    assertTrue(existingCheckin.isPresent(), "O carro deveria estar estacionado, mas não está!");
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
    Optional<SlotModel> updatedSlot = jpaSlotRepositoryMock.findById((long) slotId);
    assertTrue(updatedSlot.isPresent(), "O slot com ID " + slotId + " não foi encontrado.");
    assertTrue(updatedSlot.get().isOccupied(), "O slot com ID " + slotId + " deveria estar ocupado, mas não está.");
  }

  @Entao("o status da resposta do checkin deve ser {int}")
  public void theResponseStatusShouldBe(int status) throws Exception {
    CheckinResponseDTO checkinResponseDTO = objectMapper.readValue(responseContent, CheckinResponseDTO.class);
    ApiErrorDTO error = objectMapper.readValue(responseContent, ApiErrorDTO.class);
    switch (HttpStatus.valueOf(status)) {
      case CREATED:
        assertNotNull(checkinResponseDTO.id());
        assertNotNull(checkinResponseDTO.checkInTime());
        assertEquals(status, responseStatus.value());
        break;
      case UNPROCESSABLE_ENTITY:
        assertNotNull(error.getErrors());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(), error.getError());
        assertEquals("Validation error", error.getMessage());
        assertEquals("Plate cannot be empty", error.getErrors().get("plate"));
        assertEquals(status, responseStatus.value());
        break;
      case BAD_REQUEST:
        assertEquals(status, responseStatus.value());
        break;
      default:
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseStatus.value());
        break;
    }
  }

  @E("a resposta deve conter um timestamp de check-in")
  public void theResponseShouldContainACheckInTimestamp() throws Exception {
    CheckinResponseDTO checkinResponseDTO = objectMapper.readValue(responseContent, CheckinResponseDTO.class);
    assertNotNull(checkinResponseDTO.checkInTime(), "Checkin timestamp não foi retornado na resposta.");
  }

}