package com.pablords.parking.integration.CT003;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.nio.file.Files;
import java.nio.file.Paths;

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
import com.pablords.parking.adapters.inbound.http.dtos.CheckoutResponseDTO;
import com.pablords.parking.core.ports.inbound.services.CheckinServicePort;
import com.pablords.parking.core.ports.outbound.repositories.CheckinRepositoryPort;

import io.cucumber.java.Before;

import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class CheckoutSteps {
  @Autowired
  private WebApplicationContext webApplicationContext;
  private MockMvc mockMvc;
  private HttpStatus responseStatus;
  private String responseContent;
  private final String PARKING_API_URL_CHECKOUTS = "/checkouts";
  @Autowired
  private JdbcTemplate jdbcTemplate;
  @Autowired
  CheckinServicePort checkinServicePort;
  @Autowired
  CheckinRepositoryPort checkinRepositoryPort;

  @Value("${features.request.path}")
  private String featuresRequestPath;

  private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

  @Before
  public void setUp() {
    // Limpando a tabelas do banco H2
    jdbcTemplate.execute("DELETE FROM checkouts");
    jdbcTemplate.execute("DELETE FROM checkins");
    jdbcTemplate.execute("DELETE FROM slots");

    jdbcTemplate.execute(
        "CREATE TABLE IF NOT EXISTS checkins (id VARCHAR(255), plate VARCHAR(255), brand VARCHAR(255), color VARCHAR(255), model VARCHAR(255), checkin_time TIMESTAMP)");
    jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS slots (id INTEGER, occupied BOOLEAN)");
    jdbcTemplate.execute(
        "CREATE TABLE IF NOT EXISTS cars (plate VARCHAR(255), brand VARCHAR(255), color VARCHAR(255), model VARCHAR(255))");
    jdbcTemplate.execute(
        "CREATE TABLE IF NOT EXISTS checkouts (id VARCHAR(255), checkin_id VARCHAR(255), checkout_time TIMESTAMP)");
    // Inserindo um slot disponível
    jdbcTemplate.execute("INSERT INTO slots (id, occupied) VALUES (1, false)");
    jdbcTemplate.execute("INSERT INTO slots (id,occupied) VALUES (2, false)");

    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
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
  public void theClientSendsACheckInInvalidRequestWith(String jsonPath) throws Exception {

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
    switch (HttpStatus.valueOf(status)) {
      case CREATED:
        org.junit.jupiter.api.Assertions.assertNotNull(checkoutResponseDTO.checkin().id());
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
  public void theSlotWithIdMustBeReleased(Integer slotId) {
    var isOccupied = jdbcTemplate.queryForObject("SELECT occupied FROM slots WHERE id = ?", Boolean.class, slotId);
    assertTrue(!isOccupied, "O slot não foi liberado.");
  }

  @E("a resposta deve conter um timestamp de checkout")
  public void the_response_should_contain_a_check_in_timestamp() throws Exception {
    CheckoutResponseDTO checkoutResponseDTO = objectMapper.readValue(responseContent, CheckoutResponseDTO.class);
    assertNotNull(checkoutResponseDTO.checkOutTime(), "Checkout timestamp não foi retornado na resposta.");
  }

}
