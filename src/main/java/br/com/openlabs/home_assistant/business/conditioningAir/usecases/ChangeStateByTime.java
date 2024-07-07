package br.com.openlabs.home_assistant.business.conditioningAir.usecases;

import br.com.openlabs.home_assistant.business.conditioningAir.usecases.dtos.AirConditionerStateTimeDTO;
import br.com.openlabs.home_assistant.infra.persistence.conditioningAir.AirConditionerPersistence;
import org.springframework.stereotype.Service;

@Service
public class ChangeStateByTime {

    private final AirConditionerPersistence airConditionerPersistence;

    public ChangeStateByTime(AirConditionerPersistence airConditionerPersistence) {
        this.airConditionerPersistence = airConditionerPersistence;
    }

    public void execute(AirConditionerStateTimeDTO airConditionerStateTimeDTO, long id) {
        airConditionerPersistence.findById(id)
                .map(conditioningAir -> {
                    conditioningAir.setTimes(
                            airConditionerStateTimeDTO.turnOffTime(),
                            airConditionerStateTimeDTO.turnOnTime()
                    );
                    return airConditionerPersistence.save(conditioningAir);
                })
                .orElseThrow(() -> new RuntimeException("Conditioning air not found"));
    }
}
