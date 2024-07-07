package br.com.openlabs.home_assistant.business.conditioningAir.util;

import br.com.openlabs.home_assistant.business.conditioningAir.ConditioningAir;
import br.com.openlabs.home_assistant.business.conditioningAir.usecases.dtos.AirConditionerInfoDTO;

public class ConditioningAirAndDtoConverter {

    public static AirConditionerInfoDTO toConditioningAirInformationDTO(ConditioningAir conditioningAir) {
        return new AirConditionerInfoDTO(
                conditioningAir.getName(),
                conditioningAir.getState(),
                conditioningAir.getTemperature(),
                conditioningAir.getTurnOffTime(),
                conditioningAir.getTurnOnTime(),
                conditioningAir.getCidadeId()
        );
    }

    public static ConditioningAir toConditioningAir(AirConditionerInfoDTO airConditionerInfoDTO) {
        return new ConditioningAir(
                airConditionerInfoDTO.name(),
                airConditionerInfoDTO.state(),
                airConditionerInfoDTO.temperature(),
                airConditionerInfoDTO.turnOffTime(),
                airConditionerInfoDTO.turnOnTime(),
                airConditionerInfoDTO.cidadeId()
        );
    }
}
