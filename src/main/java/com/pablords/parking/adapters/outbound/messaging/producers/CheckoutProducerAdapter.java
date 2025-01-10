package com.pablords.parking.adapters.outbound.messaging.producers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pablords.parking.core.entities.Car;
import com.pablords.parking.core.entities.Checkout;

import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.pablords.parking.adapters.outbound.messaging.config.RabbitMQConfig.EXCHANGE_NAME;
import static com.pablords.parking.adapters.outbound.messaging.config.RabbitMQConfig.QUEUE_NAME;

@Component
@Slf4j
public class CheckoutProducerAdapter {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public CheckoutProducerAdapter(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendCheckoutMessage(Checkout checkout) {
        Map<String, Object> message = new HashMap<>();
        Car car = checkout.getCheckin().getCar();
        message.put("car", car);
        message.put("parkingFee", checkout.getParkingFee());
        message.put("timestamp", LocalDateTime.now());

        log.debug("Publicando mensagem na fila: {}, com o payload: {}", QUEUE_NAME, message);

        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "checkoutRoutingKey", jsonMessage);
            log.debug("Mensagem publicada: {}, {}", QUEUE_NAME, jsonMessage);
        } catch (JsonProcessingException e) {
            log.error("Erro ao publicar mensagem serializada: {}", e.getMessage());
        }
    }
}
