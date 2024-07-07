package br.com.openlabs.home_assistant.business.conditioningAir.usecases;

import br.com.openlabs.home_assistant.business.conditioningAir.ConditioningAir;
import br.com.openlabs.home_assistant.business.conditioningAir.usecases.dtos.AirConditionerInfoDTO;
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
        Optional<ConditioningAir> conditioningAir = airConditionerPersistence.findById(id);
        if (conditioningAir.isPresent()) {
            return new AirConditionerInfoDTO(
                    conditioningAir.get().getName(),
                    conditioningAir.get().getState(),
                    conditioningAir.get().getTemperature(),
                    conditioningAir.get().getTurnOffTime(),
                    conditioningAir.get().getTurnOnTime(),
                    conditioningAir.get().getCidadeId()
            );
        }

        return null;
    }

}
