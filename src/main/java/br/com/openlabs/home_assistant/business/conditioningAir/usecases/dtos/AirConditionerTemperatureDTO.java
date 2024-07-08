package br.com.openlabs.home_assistant.business.conditioningAir.usecases.dtos;

import jakarta.validation.constraints.NotNull;

public record AirConditionerTemperatureDTO(
        @NotNull(message = "Temperature must not be null")
        Integer temperature) {
}
