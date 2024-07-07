package br.com.openlabs.home_assistant.business.conditioningAir.usecases.dtos;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record AirConditionerStateTimeDTO(
        @NotNull
        LocalTime turnOffTime,
        @NotNull
        LocalTime turnOnTime){
}
