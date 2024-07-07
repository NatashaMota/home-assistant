package br.com.openlabs.home_assistant.business.conditioningAir.usecases;

import br.com.openlabs.home_assistant.business.conditioningAir.ConditioningAir;
import br.com.openlabs.home_assistant.business.conditioningAir.usecases.dtos.AirConditionerInfoDTO;
import br.com.openlabs.home_assistant.business.conditioningAir.util.ConditioningAirAndDtoConverter;
import br.com.openlabs.home_assistant.infra.persistence.conditioningAir.AirConditionerPersistence;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreateConditioningAir {

    private final AirConditionerPersistence airConditionerPersistence;

    public CreateConditioningAir(AirConditionerPersistence airConditionerPersistence) {
        this.airConditionerPersistence = airConditionerPersistence;
    }

    public Long execute(AirConditionerInfoDTO airConditionerInfoDTO) {
        return Optional.ofNullable(airConditionerInfoDTO)
                .map(ConditioningAirAndDtoConverter::toConditioningAir)
                .map(airConditionerPersistence::save)
                .map(ConditioningAir::getId)
                .orElseThrow(() -> new RuntimeException("Failed to create Conditioning Air"));
    }

}
