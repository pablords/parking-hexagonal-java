package com.pablords.parking.component.CT002;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pablords.parking.adapters.inbound.http.dtos.CheckinResponseDTO;
import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.entities.Checkin;
import com.pablords.parking.core.entities.Checkout;
import com.pablords.parking.core.entities.Slot;
import com.pablords.parking.core.ports.outbound.repositories.CheckinRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.CheckoutRepositoryPort;
import com.pablords.parking.core.ports.outbound.repositories.SlotRepositoryPort;
import com.pablords.parking.core.valueobjects.Plate;

import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class CheckinSteps {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CheckinRepositoryPort checkinRepositoryPortMock;
    @Autowired
    private SlotRepositoryPort slotRepositoryPortMock;
    @Autowired
    private CheckoutRepositoryPort checkoutRepositoryPortMock;
    @Autowired
    private Environment environment;

    private Checkin createdCheckin;
    private Car car;
    private ArrayList<Slot> slots;
    private Checkout checkout;

    private HttpStatus responseStatus;
    private String responseContent;
    private final String PARKING_API_URL_CHECKINS = "/checkins";
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Before
    public void setUp() {
        System.out.println("🔎 Active Profiles: " + String.join(", ", environment.getActiveProfiles()));

        // Mockando Slots disponíveis
        slots = new ArrayList<Slot>();
        for (int i = 1; i <= 2; i++) {
            var slot = new Slot();
            slot.setId((long) i);
            slot.setOccupied(false);
            slots.add(slot);
        }
        when(slotRepositoryPortMock.findAvailableSlot()).thenReturn(Optional.of(slots.get(0)));
        when(slotRepositoryPortMock.findById(any())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return slots.stream().filter(slot -> slot.getId().equals(id)).findFirst();
        });
        when(slotRepositoryPortMock.save(any(Slot.class))).thenReturn(slots.get(0));
    }

    @Dado("que o carro com placa {string} não está estacionado")
    public void the_car_with_plate_is_not_checked_in(String plate) {

        // Mockando Checkins
        createdCheckin = new Checkin();
        car = new Car(new Plate(plate), "Audi", "Black", "A4");
        createdCheckin.setCar(car);
        createdCheckin.setId(UUID.fromString("f5d4b3b4-1b4b-4b4b-8b4b-4b4b4b4b4b4b"));
        createdCheckin.setSlot(slots.get(0));
        createdCheckin.setCheckInTime(LocalDateTime.now());
        when(checkinRepositoryPortMock.save(any(Checkin.class))).thenReturn(createdCheckin);
        when(checkinRepositoryPortMock.findByPlate(any())).thenReturn(Optional.ofNullable(null));
        when(checkinRepositoryPortMock.findById(any())).thenReturn(Optional.ofNullable(null));

        // Mockando Checkout
        checkout = new Checkout(createdCheckin);
        when(checkoutRepositoryPortMock.findByCheckinId(any())).thenReturn(Optional.of(checkout));
        when(checkoutRepositoryPortMock.save(any(Checkout.class))).thenReturn(checkout);
        assertEquals("ABC1234", plate);
    }


    @Dado("que o carro com placa {string} está estacionado")
    public void the_car_with_plate_is_checked_in(String plate) {
        slots.get(1).setOccupied(true);
        when(slotRepositoryPortMock.findAvailableSlot()).thenReturn(Optional.of(slots.get(1)));

        createdCheckin = new Checkin();
        car = new Car(new Plate(plate), "Audi", "Black", "A4");
        createdCheckin.setCar(car);
        createdCheckin.setId(UUID.fromString("f5d4b3b4-1b4b-4b4b-8b4b-4b4b4b4b4b4b"));
        createdCheckin.setSlot(slots.get(1));
        createdCheckin.setCheckInTime(LocalDateTime.now());

        checkout = new Checkout(createdCheckin);
        checkout.setCheckin(createdCheckin);
        checkout.setId(UUID.fromString("f5d4b3b4-1b4b-4b4b-8b4b-4b4b4b4b4b4b"));

        when(checkinRepositoryPortMock.findByPlate(any())).thenReturn(Optional.of(createdCheckin));
        when(checkoutRepositoryPortMock.findByCheckinId(createdCheckin.getId())).thenReturn(Optional.empty());
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
