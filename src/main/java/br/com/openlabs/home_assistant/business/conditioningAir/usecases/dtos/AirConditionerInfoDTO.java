package br.com.openlabs.home_assistant.business.conditioningAir.usecases.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalTime;

public record AirConditionerInfoDTO(
        @NotBlank(message = "Name must not be blank")
        String name,
        @NotNull(message = "State must not be null")
        Boolean state,
        Integer temperature,
        @NotNull(message = "Turn off time must not be null")
        @DateTimeFormat(pattern = "HH:mm")
        LocalTime turnOffTime,
        @DateTimeFormat(pattern = "HH:mm")
        @NotNull(message = "Turn on time must not be null")
        LocalTime turnOnTime,
        @NotNull(message = "Latitude must not be null")
        Double latitude,
        @NotNull(message = "Longitude must not be null")
        Double longitude
) {
}
