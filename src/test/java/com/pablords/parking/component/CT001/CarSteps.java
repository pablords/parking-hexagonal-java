package com.pablords.parking.component.CT001;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pablords.parking.adapters.inbound.http.dtos.CarResponseDTO;
import com.pablords.parking.adapters.inbound.http.handlers.ApiErrorDTO;
import com.pablords.parking.adapters.outbound.database.jpa.models.CarModel;
import com.pablords.parking.adapters.outbound.database.jpa.repositories.springdata.JpaCarRepository;

import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class CarSteps {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private JpaCarRepository jpaCarRepositoryMock;

  private HttpStatus responseStatus;
  private String responseContent;
  private final String PARKING_API_URL_CARS = "/cars";
  private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

  @Before
  public void setUp() {
    when(jpaCarRepositoryMock.save(any(CarModel.class))).thenAnswer(invocation -> {
      CarModel car = invocation.getArgument(0);
      car.setId(UUID.fromString("f7f6b3e3-4b7b-4b7b-8b7b-4b7b7b7b7b7b"));
      return car;
    });
  }

  @Dado("que estou no endpoint da API {string}")
  public void thatIAmInTheApiEndpoint(String endpoint) {
    assertEquals(endpoint, PARKING_API_URL_CARS);
  }

  @Quando("eu crio um carro com os seguintes detalhes: {string}")
  public void iCreateACarWithTheFollowingDetails(String jsonPath) throws Exception {
    var jsonFileContent = new String(Files.readAllBytes(Paths.get(jsonPath)));

    mockMvc.perform(post(PARKING_API_URL_CARS)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonFileContent))
        .andExpect(result -> {
          responseStatus = HttpStatus.valueOf(result.getResponse().getStatus());
          responseContent = result.getResponse().getContentAsString();
        });
  }

  @Entao("o status da resposta do carro deve ser {int}")
  public void theResponseStatusShouldBe(int status) throws Exception {
    CarResponseDTO carResponseDTO = objectMapper.readValue(responseContent, CarResponseDTO.class);
    ApiErrorDTO error = objectMapper.readValue(responseContent, ApiErrorDTO.class);

    switch (HttpStatus.valueOf(status)) {
      case CREATED:
        assertNotNull(carResponseDTO.id());
        assertEquals("f7f6b3e3-4b7b-4b7b-8b7b-4b7b7b7b7b7b", carResponseDTO.id().toString());
        assertEquals("ABC1234", carResponseDTO.plate());
        assertEquals("Audi", carResponseDTO.brand());
        assertEquals("Black", carResponseDTO.color());
        assertEquals("A4", carResponseDTO.model());
        assertEquals(status, responseStatus.value());
        break;
      case UNPROCESSABLE_ENTITY:
        assertNotNull(error.getErrors());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(), error.getError());
        assertEquals("Validation error", error.getMessage());
        assertEquals("Plate cannot be empty", error.getErrors().get("plate"));
        assertEquals(status, responseStatus.value());
        break;
      default:
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseStatus.value());
        break;
    }
  }

}