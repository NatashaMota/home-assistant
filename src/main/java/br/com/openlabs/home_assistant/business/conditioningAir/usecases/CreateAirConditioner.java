package br.com.openlabs.home_assistant.business.conditioningAir.usecases;

import br.com.openlabs.home_assistant.business.conditioningAir.ConditionerAir;
import br.com.openlabs.home_assistant.business.conditioningAir.usecases.dtos.AirConditionerInfoDTO;
import br.com.openlabs.home_assistant.business.conditioningAir.util.AirConditionerAndDtoConverter;
import br.com.openlabs.home_assistant.infra.persistence.conditioningAir.AirConditionerPersistence;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreateAirConditioner {

    private final AirConditionerPersistence airConditionerPersistence;

    public CreateAirConditioner(AirConditionerPersistence airConditionerPersistence) {
        this.airConditionerPersistence = airConditionerPersistence;
    }

    public Long execute(AirConditionerInfoDTO airConditionerInfoDTO) {
        return Optional.ofNullable(airConditionerInfoDTO)
                .map(AirConditionerAndDtoConverter::toConditioningAir)
                .map(airConditionerPersistence::save)
                .map(ConditionerAir::getId)
                .orElseThrow(() -> new RuntimeException("Failed to create Conditioning Air"));
    }

}
