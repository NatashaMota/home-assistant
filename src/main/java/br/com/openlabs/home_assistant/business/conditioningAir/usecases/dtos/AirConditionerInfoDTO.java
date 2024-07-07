package br.com.openlabs.home_assistant.business.conditioningAir.usecases.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record AirConditionerInfoDTO(
        @NotBlank
        String name,
        @NotNull
        Boolean state,
        Integer temperature,

        LocalTime turnOffTime,
        LocalTime turnOnTime,
        @NotNull
        Long Latitude,
        @NotNull
        Long longitude
) {
}
