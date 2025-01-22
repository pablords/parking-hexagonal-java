package com.pablords.parking.component.CT002;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pablords.parking.adapters.inbound.http.dtos.CheckinResponseDTO;
import com.pablords.parking.adapters.outbound.database.jpa.models.CarModel;
import com.pablords.parking.adapters.outbound.database.jpa.models.CheckinModel;
import com.pablords.parking.adapters.outbound.database.jpa.models.CheckoutModel;
import com.pablords.parking.adapters.outbound.database.jpa.models.SlotModel;
import com.pablords.parking.adapters.outbound.database.jpa.repositories.JpaCheckinRepository;
import com.pablords.parking.adapters.outbound.database.jpa.repositories.JpaCheckoutRepository;
import com.pablords.parking.adapters.outbound.database.jpa.repositories.JpaSlotRepository;

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
        // Mockando Slots disponíveis
        slots = new ArrayList<SlotModel>();
        for (int i = 1; i <= 2; i++) {
            var slot = new SlotModel();
            slot.setId((long) i);
            slot.setOccupied(false);
            slots.add(slot);
        }

        car = new CarModel();
        car.setPlate("ABC1234");
        car.setBrand("Audi");
        car.setColor("Black");
        car.setModel("A4");
        car.setId(UUID.fromString("f5d4b3b4-1b4b-4b4b-8b4b-4b4b4b4b4b4b"));
        checkout = new CheckoutModel();
        createdCheckin = new CheckinModel();
        createdCheckin.setCar(car);
        createdCheckin.setId(UUID.fromString("f5d4b3b4-1b4b-4b4b-8b4b-4b4b4b4b4b4b"));
        createdCheckin.setCheckInTime(LocalDateTime.now());

        // Mock para buscar o primeiro slot disponível
        when(jpaSlotRepositoryMock.findFirstByOccupiedFalse()).thenReturn(Optional.of(slots.get(0)));

        // Mock para buscar um slot pelo ID
        when(jpaSlotRepositoryMock.findById(any())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return slots.stream().filter(slot -> slot.getId().equals(id)).findFirst();
        });

        // Mock para salvar um slot e atualizar a ocupação corretamente
        when(jpaSlotRepositoryMock.save(any(SlotModel.class))).thenAnswer(invocation -> {
            SlotModel slot = invocation.getArgument(0);
            slots.stream().filter(s -> s.getId().equals(slot.getId())).findFirst()
                    .ifPresent(s -> s.setOccupied(slot.isOccupied()));
            return slot;
        });
    }

    @Dado("que o carro com placa {string} não está estacionado")
    public void the_car_with_plate_is_not_checked_in(String plate) {
        // Mockando Checkins
        createdCheckin.setSlot(slots.get(0));
        when(jpaCheckinRepositoryMock.save(any(CheckinModel.class))).thenReturn(createdCheckin);
        when(jpaCheckinRepositoryMock.findByCarPlate(any())).thenReturn(Optional.ofNullable(null));
        when(jpaCheckinRepositoryMock.findById(any())).thenReturn(Optional.ofNullable(null));

        // Mockando Checkout
        when(jpaCheckoutRepositoryMock.findByCheckinId(any())).thenReturn(Optional.of(checkout));
        when(jpaCheckoutRepositoryMock.save(any(CheckoutModel.class))).thenReturn(checkout);

        Optional<CheckinModel> existingCheckin = jpaCheckinRepositoryMock.findByCarPlate(plate);
        assertTrue(!existingCheckin.isPresent(), "O carro não deveria estar estacionado, mas está!");
    }

    @Dado("que o carro com placa {string} está estacionado")
    public void the_car_with_plate_is_checked_in(String plate) {
        slots.get(1).setOccupied(true);
        when(jpaSlotRepositoryMock.findFirstByOccupiedFalse()).thenReturn(Optional.of(slots.get(1)));
        createdCheckin.setSlot(slots.get(0));
        checkout.setId(UUID.fromString("f5d4b3b4-1b4b-4b4b-8b4b-4b4b4b4b4b4b"));
        when(jpaCheckinRepositoryMock.findByCarPlate(any())).thenReturn(Optional.of(createdCheckin));
        when(jpaCheckoutRepositoryMock.findByCheckinId(createdCheckin.getId())).thenReturn(Optional.empty());
        Optional<CheckinModel> existingCheckin = jpaCheckinRepositoryMock.findByCarPlate(plate);
        assertTrue(existingCheckin.isPresent(), "O carro deveria estar estacionado, mas não está!");
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

    @Entao("o slot com id {int} deve ser ocupado")
    public void theSlotWithIdShouldBeOccupied(int slotId) {
        Optional<SlotModel> updatedSlot = jpaSlotRepositoryMock.findById((long) slotId);
        assertTrue(updatedSlot.isPresent(), "O slot com ID " + slotId + " não foi encontrado.");
        assertTrue(updatedSlot.get().isOccupied(), "O slot com ID " + slotId + " deveria estar ocupado, mas não está.");
    }

    @Entao("o status da resposta do checkin deve ser {int}")
    public void the_response_status_should_be(int status) throws Exception {
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
        assertNotNull(checkinResponseDTO.getCheckInTime(), "Checkin timestamp não foi retornado na resposta.");
    }

}
