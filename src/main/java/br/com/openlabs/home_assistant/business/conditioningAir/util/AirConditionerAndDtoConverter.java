package br.com.openlabs.home_assistant.business.conditioningAir.util;

import br.com.openlabs.home_assistant.business.conditioningAir.AirConditioner;
import br.com.openlabs.home_assistant.business.conditioningAir.usecases.dtos.AirConditionerInfoDTO;

public class AirConditionerAndDtoConverter {

    public static AirConditionerInfoDTO toConditioningAirInformationDTO(AirConditioner airConditioner) {
        return new AirConditionerInfoDTO(
                airConditioner.getName(),
                airConditioner.getState(),
                airConditioner.getTemperature(),
                airConditioner.getTurnOffTime(),
                airConditioner.getTurnOnTime(),
                airConditioner.getLatitude(),
                airConditioner.getLongitude()
        );
    }

    public static AirConditioner toConditioningAir(AirConditionerInfoDTO airConditionerInfoDTO) {
        return new AirConditioner(
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
