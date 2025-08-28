package com.pablords.parking.adapters.inbound.http.dtos;

import java.time.LocalDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pablords.parking.core.entities.Slot;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CheckinResponseDTO(
  @JsonProperty UUID id,
  @JsonProperty Slot slot,
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC") LocalDateTime checkInTime
) {}
