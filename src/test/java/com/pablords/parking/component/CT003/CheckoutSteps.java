package com.pablords.parking.component.CT003;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import com.pablords.parking.adapters.inbound.http.dtos.CheckoutResponseDTO;
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
import com.pablords.parking.core.ports.outbound.producers.CheckoutProducerPort;

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
  public void a_car_with_payload(String jsonPath) throws Exception {
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
  public void the_client_sends_a_check_in_invalid_request_with(String jsonPath) throws Exception {
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
  public void the_response_status_should_be(int status) throws Exception {
    CheckoutResponseDTO checkoutResponseDTO = objectMapper.readValue(responseContent, CheckoutResponseDTO.class);
    System.out.println("Response: " + responseContent);
    System.out.println("Status: " + status);
    System.out.println("Response Status: " + responseStatus.value());
    System.out.println("CheckoutResponseDTO: " + checkoutResponseDTO);
    switch (HttpStatus.valueOf(status)) {
      case CREATED:
        assertNotNull(checkoutResponseDTO.getCheckOutTime(), "Checkout timestamp não foi retornado na resposta.");
        assertEquals(responseStatus.value(), status);
        break;
      case UNPROCESSABLE_ENTITY:
        assertEquals(responseStatus.value(), status);
        break;
      case BAD_REQUEST:
        assertEquals(responseStatus.value(), status);
        break;
      case NOT_FOUND:
        assertEquals(responseStatus.value(), status);
        break;
      default:
        assertEquals(responseStatus.value(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        break;
    }
  }

  @E("o slot com id {int} deve ser liberado")
  public void o_slot_com_id_deve_ser_liberado(Integer slotId) {
    SlotModel slot = slots.stream().filter(s -> s.getId() == slotId.longValue()).findFirst().get();
    assertTrue("O slot não foi liberado.", !slot.isOccupied());
  }

  @E("a resposta deve conter um timestamp de checkout")
  public void the_response_should_contain_a_check_in_timestamp() throws Exception {
    CheckoutResponseDTO checkoutResponseDTO = objectMapper.readValue(responseContent, CheckoutResponseDTO.class);
    assertNotNull(checkoutResponseDTO.getCheckOutTime(), "Checkout timestamp não foi retornado na resposta.");
  }

}
