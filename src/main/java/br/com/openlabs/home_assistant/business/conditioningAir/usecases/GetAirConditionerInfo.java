package br.com.openlabs.home_assistant.business.conditioningAir.usecases;

import br.com.openlabs.home_assistant.business.conditioningAir.AirConditioner;
import br.com.openlabs.home_assistant.business.conditioningAir.usecases.dtos.AirConditionerInfoDTO;
import br.com.openlabs.home_assistant.business.conditioningAir.util.AirConditionerAndDtoConverter;
import br.com.openlabs.home_assistant.infra.persistence.conditioningAir.AirConditionerPersistence;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetAirConditionerInfo {
    private final AirConditionerPersistence airConditionerPersistence;


    public GetAirConditionerInfo(AirConditionerPersistence airConditionerPersistence) {
        this.airConditionerPersistence = airConditionerPersistence;
    }

    public AirConditionerInfoDTO execute(Long id) {
        Optional<AirConditioner> conditioningAir = airConditionerPersistence.findById(id);
        return conditioningAir.map(AirConditionerAndDtoConverter::toConditioningAirInformationDTO).orElse(null);

    }

}
