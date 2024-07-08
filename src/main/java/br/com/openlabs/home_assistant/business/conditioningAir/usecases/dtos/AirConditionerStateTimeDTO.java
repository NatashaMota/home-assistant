package br.com.openlabs.home_assistant.business.conditioningAir.usecases.dtos;

import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

public record AirConditionerStateTimeDTO(
        @NotNull(message = "Turn off time must not be null")
        @DateTimeFormat(pattern = "HH:mm")
        LocalTime turnOffTime,
        @NotNull(message = "Turn on time must not be null")
        @DateTimeFormat(pattern = "HH:mm")
        LocalTime turnOnTime){
}
