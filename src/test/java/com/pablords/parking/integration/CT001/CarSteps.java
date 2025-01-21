package com.pablords.parking.integration.CT001;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pablords.parking.adapters.inbound.http.dtos.CarResponseDTO;

import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class CarSteps {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private HttpStatus responseStatus;
    private String responseContent;
    private final String PARKING_API_URL_CARS = "/cars";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
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
        switch (responseStatus) {
            case CREATED:
                assertNotNull(carResponseDTO.getId());
                assertEquals(responseStatus.value(), status);
                break;
            case UNPROCESSABLE_ENTITY:
                assertEquals(responseStatus.value(), status);
                break;
            default:
                assertEquals(responseStatus.value(), HttpStatus.INTERNAL_SERVER_ERROR.value());
                break;
        }

    }

}
