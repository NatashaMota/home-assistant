package br.com.openlabs.home_assistant.business.conditioningAir.util;

import br.com.openlabs.home_assistant.business.conditioningAir.ConditionerAir;
import br.com.openlabs.home_assistant.business.conditioningAir.usecases.dtos.AirConditionerInfoDTO;

public class AirConditionerAndDtoConverter {

    public static AirConditionerInfoDTO toConditioningAirInformationDTO(ConditionerAir conditionerAir) {
        return new AirConditionerInfoDTO(
                conditionerAir.getName(),
                conditionerAir.getState(),
                conditionerAir.getTemperature(),
                conditionerAir.getTurnOffTime(),
                conditionerAir.getTurnOnTime(),
                conditionerAir.getLatitude(),
                conditionerAir.getLongitude()
        );
    }

    public static ConditionerAir toConditioningAir(AirConditionerInfoDTO airConditionerInfoDTO) {
        return new ConditionerAir(
                airConditionerInfoDTO.name(),
                airConditionerInfoDTO.state(),
                airConditionerInfoDTO.temperature(),
                airConditionerInfoDTO.turnOffTime(),
                airConditionerInfoDTO.turnOnTime(),
                airConditionerInfoDTO.latitude(),
                airConditionerInfoDTO.longitude()
        );
    }
}
