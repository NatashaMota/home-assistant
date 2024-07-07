package br.com.openlabs.home_assistant.business.conditioningAir.usecases.dtos;

import jakarta.validation.constraints.NotNull;

public record AirConditionerTemperatureDTO(
        @NotNull
        Integer temperature) {
}
