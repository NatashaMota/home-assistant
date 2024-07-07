package br.com.openlabs.home_assistant.business.conditioningAir.usecases;

import br.com.openlabs.home_assistant.business.conditioningAir.usecases.dtos.AirConditionerTemperatureDTO;
import br.com.openlabs.home_assistant.infra.persistence.conditioningAir.AirConditionerPersistence;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AdjustTemperature {

    private final AirConditionerPersistence airConditionerPersistence;

    public AdjustTemperature(AirConditionerPersistence airConditionerPersistence) {
        this.airConditionerPersistence = airConditionerPersistence;
    }

    public void execute(@PathVariable Long id, @RequestBody AirConditionerTemperatureDTO airConditionerTemperatureDTO) {
        airConditionerPersistence.findById(id)
                .map(conditioningAir -> {
                    conditioningAir.setTemperature(airConditionerTemperatureDTO.temperature());
                    return airConditionerPersistence.save(conditioningAir);
                })
                .orElseThrow(() -> new RuntimeException("Conditioning air not found"));
    }

}
