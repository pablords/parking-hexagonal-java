package com.pablords.parking.component.CT003;

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
import com.pablords.parking.adapters.inbound.http.dtos.CheckoutResponseDTO;
import com.pablords.parking.adapters.inbound.http.handlers.ApiError;
import com.pablords.parking.adapters.outbound.database.jpa.models.CarModel;
import com.pablords.parking.adapters.outbound.database.jpa.models.CheckinModel;
import com.pablords.parking.adapters.outbound.database.jpa.models.CheckoutModel;
import com.pablords.parking.adapters.outbound.database.jpa.models.SlotModel;
import com.pablords.parking.adapters.outbound.database.jpa.repositories.JpaCarRepository;
import com.pablords.parking.adapters.outbound.database.jpa.repositories.JpaCheckinRepository;
import com.pablords.parking.adapters.outbound.database.jpa.repositories.JpaCheckoutRepository;
import com.pablords.parking.adapters.outbound.database.jpa.repositories.JpaSlotRepository;
import com.pablords.parking.adapters.outbound.messaging.producers.CheckoutProducerAdapter;
import com.pablords.parking.component.TestUtils;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class CheckoutSteps {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private JpaSlotRepository jpaSlotRepositoryMock;
  @Autowired
  private JpaCheckinRepository jpaCheckinRepositoryMock;
  @Autowired
  private JpaCarRepository jpaCarRepositoryMock;
  @Autowired
  private JpaCheckoutRepository jpaCheckoutRepositoryMock;
  @Autowired
  private CheckoutProducerAdapter checkoutProducerAdapterMock;

  private CheckinModel createdCheckin;
  private ArrayList<SlotModel> slots;
  private CheckoutModel checkout;
  private CarModel car;

  private HttpStatus responseStatus;
  private String responseContent;
  private final String PARKING_API_URL_CHECKOUTS = "/checkouts";
  private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

  @Before
  public void setUp() {
    slots = TestUtils.createSlots();
    car = TestUtils.createCarModel();
    createdCheckin = TestUtils.createCheckinModel(car, slots);
    checkout = TestUtils.createCheckoutModel(createdCheckin);
    TestUtils.mockSlotRepository(jpaSlotRepositoryMock, slots);
    when(jpaCarRepositoryMock.findByPlate(any())).thenReturn(Optional.of(car));
    when(jpaCheckinRepositoryMock.findByCarPlate(any())).thenReturn(Optional.of(createdCheckin));
    when(jpaCheckinRepositoryMock.save(any())).thenReturn(createdCheckin);
    Mockito.doNothing().when(checkoutProducerAdapterMock).sendCheckoutMessage(any());
    when(jpaCheckoutRepositoryMock.save(any())).thenReturn(checkout);
  }

  @After
  public void tearDown() {
    slots.clear();
    car = null;
    createdCheckin = null;
    checkout = null;
    Mockito.reset();
  }

  @Quando("o cliente envia uma solicitação de checkout com a placa {string}")
  public void aCarWithPayload(String jsonPath) throws Exception {
    var jsonFileContent = new String(Files.readAllBytes(Paths.get(jsonPath)));
    mockMvc.perform(post(PARKING_API_URL_CHECKOUTS)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonFileContent))
        .andExpect(result -> {
          responseStatus = HttpStatus.valueOf(result.getResponse().getStatus());
          responseContent = result.getResponse().getContentAsString();
        });
  }

  @Quando("o cliente envia uma solicitação de checkout inválida com {string}")
  public void theClientSendsACheckoutInvalidRequestWith(String jsonPath) throws Exception {
    var jsonFileContent = new String(Files.readAllBytes(Paths.get(jsonPath)));
    mockMvc.perform(post(PARKING_API_URL_CHECKOUTS)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonFileContent))
        .andExpect(result -> {
          responseStatus = HttpStatus.valueOf(result.getResponse().getStatus());
          responseContent = result.getResponse().getContentAsString();
        });
  }

  @Entao("o status da resposta do checkout deve ser {int}")
  public void theResponseStatusShouldBe(int status) throws Exception {
    CheckoutResponseDTO checkoutResponseDTO = objectMapper.readValue(responseContent, CheckoutResponseDTO.class);
    ApiError error = objectMapper.readValue(responseContent, ApiError.class);
    switch (HttpStatus.valueOf(status)) {
      case CREATED:
        assertNotNull(checkoutResponseDTO.getCheckOutTime(), "Checkout timestamp não foi retornado na resposta.");
        assertEquals(status, responseStatus.value());
        break;
      case UNPROCESSABLE_ENTITY:
        assertEquals(status, responseStatus.value());
        break;
      case BAD_REQUEST:
      assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), error.getError());
        assertEquals(status, responseStatus.value());
        break;
      case NOT_FOUND:
        assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), error.getError());
        assertEquals("Not Found", error.getError());
        assertEquals("No check-in found for plate: ABC1234", error.getMessage());
        assertEquals(status, responseStatus.value());
        break;
      default:
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseStatus.value());
        break;
    }
  }

  @E("o slot com id {int} deve ser liberado")
  public void theSlotWithIdShouldBeReleased(Integer slotId) {
    SlotModel slot = slots.stream().filter(s -> s.getId() == slotId.longValue()).findFirst().get();
    assertTrue(!slot.isOccupied(), "O slot não foi liberado.");
  }

  @E("a resposta deve conter um timestamp de checkout")
  public void theResponseShouldContainACheckoutTimestamp() throws Exception {
    CheckoutResponseDTO checkoutResponseDTO = objectMapper.readValue(responseContent, CheckoutResponseDTO.class);
    assertNotNull(checkoutResponseDTO.getCheckOutTime(), "Checkout timestamp não foi retornado na resposta.");
  }

}