package br.com.openlabs.home_assistant.business.conditioningAir.usecases.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

public record AirConditionerIdDTO (
        Long id,
        String name,
        Boolean state,
        Integer temperature,
        @DateTimeFormat(pattern = "HH:mm")
        LocalTime turnOffTime,
        @DateTimeFormat(pattern = "HH:mm")
        LocalTime turnOnTime,
        Double latitude,
        Double longitude
){
}
